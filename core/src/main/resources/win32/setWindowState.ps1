param (
    [Parameter(Mandatory = $true)]
    [string]$ProcessName,
    [ValidateSet('FORCEMINIMIZE', 'HIDE', 'MAXIMIZE', 'MINIMIZE', 'RESTORE',
            'SHOW', 'SHOWDEFAULT', 'SHOWMAXIMIZED', 'SHOWMINIMIZED',
            'SHOWMINNOACTIVE', 'SHOWNA', 'SHOWNOACTIVATE', 'SHOWNORMAL')]
    [string]$State = 'SHOW'
)

function Write-Log {
    param(
        [Parameter(Mandatory = $true)]
        [string]$Message,
        [ValidateSet('INFO', 'WARNING', 'ERROR', 'DEBUG')]
        [string]$Level = 'INFO'
    )

    $logTime = Get-Date -Format "yyyy-MM-dd HH:mm:ss.fff"
    $logMessage = "[$logTime] [$Level] $Message"

    # 定义日志文件路径
    $logPath = Join-Path $PSScriptRoot "WindowState.log"

    # 输出到控制台
    switch ($Level) {
        'ERROR'   { Write-Host $logMessage -ForegroundColor Red }
        'WARNING' { Write-Host $logMessage -ForegroundColor Yellow }
        'INFO'    { Write-Host $logMessage -ForegroundColor Green }
        'DEBUG'   { Write-Host $logMessage -ForegroundColor Gray }
    }

    # 写入日志文件
    Add-Content -Path $logPath -Value $logMessage
}

function Set-WindowState {
    param (
        [Parameter(Mandatory = $true)]
        [string]$ProcessName,
        [ValidateSet('FORCEMINIMIZE', 'HIDE', 'MAXIMIZE', 'MINIMIZE', 'RESTORE',
                'SHOW', 'SHOWDEFAULT', 'SHOWMAXIMIZED', 'SHOWMINIMIZED',
                'SHOWMINNOACTIVE', 'SHOWNA', 'SHOWNOACTIVATE', 'SHOWNORMAL')]
        [string]$State = 'SHOW'
    )

    Write-Log "Starting Set-WindowState function for process: $ProcessName with state: $State" -Level DEBUG

    $WindowStates = @{
        'FORCEMINIMIZE'   = 11
        'HIDE'            = 0
        'MAXIMIZE'        = 3
        'MINIMIZE'        = 6
        'RESTORE'         = 9
        'SHOW'            = 5
        'SHOWDEFAULT'     = 10
        'SHOWMAXIMIZED'   = 3
        'SHOWMINIMIZED'   = 2
        'SHOWMINNOACTIVE' = 7
        'SHOWNA'          = 8
        'SHOWNOACTIVATE'  = 4
        'SHOWNORMAL'      = 1
    }

    try {
        Write-Log "Adding Win32ShowWindow type definition" -Level DEBUG
        $Win32ShowWindow = Add-Type -MemberDefinition @'
        [DllImport("user32.dll")]
        public static extern bool ShowWindow(IntPtr hWnd, int nCmdShow);
        [DllImport("user32.dll")]
        public static extern bool SetForegroundWindow(IntPtr hWnd);
'@ -Name "Win32ShowWindow" -Namespace Win32Functions -PassThru -ErrorAction Stop
    }
    catch {
        Write-Log "Failed to add Win32ShowWindow type: $_" -Level ERROR
        return $false
    }

    $ProcessName = $ProcessName -replace '\.exe$', ''
    Write-Log "Searching for process: $ProcessName" -Level DEBUG

    try {
        $processes = Get-Process -Name $ProcessName -ErrorAction Stop
        Write-Log "Found $($processes.Count) process(es) matching name: $ProcessName" -Level INFO

        $windowFound = $false
        foreach ($proc in $processes) {
            Write-Log "Processing: $($proc.ProcessName) (ID: $($proc.Id), Title: $($proc.MainWindowTitle))" -Level DEBUG

            if ($proc.MainWindowHandle -ne 0) {
                $handle = $proc.MainWindowHandle
                Write-Log "Window handle found: $handle" -Level DEBUG

                try {
                    $showResult = $Win32ShowWindow::ShowWindow($handle, $WindowStates[$State])
                    $foregroundResult = $Win32ShowWindow::SetForegroundWindow($handle)

                    Write-Log "ShowWindow result: $showResult, SetForegroundWindow result: $foregroundResult" -Level DEBUG
                    Write-Log "Successfully set window state for process: $($proc.ProcessName) (Title: $($proc.MainWindowTitle)) -> $State" -Level INFO

                    $windowFound = $true
                }
                catch {
                    Write-Log "Failed to modify window state: $_" -Level ERROR
                }
            }
        }

        if (-not $windowFound) {
            Write-Log "No visible windows found for process: $ProcessName" -Level WARNING
            return $false
        }

        return $true
    }
    catch {
        Write-Log "Failed to find process $ProcessName : $_" -Level ERROR
        return $false
    }
}

# 脚本开始执行
Write-Log "Script started with parameters - ProcessName: $ProcessName, State: $State" -Level INFO

try {
    $result = Set-WindowState -ProcessName $ProcessName -State $State
    if ($result) {
        Write-Log "Script completed successfully" -Level INFO
    }
    else {
        Write-Log "Script completed with warnings or errors" -Level WARNING
    }
}
catch {
    Write-Log "Script failed with error: $_" -Level ERROR
}
finally {
    Write-Log "Script execution finished" -Level INFO
}
