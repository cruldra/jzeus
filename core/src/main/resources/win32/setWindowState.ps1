param (
    [Parameter(Mandatory = $true)]
    [string]$ProcessName,
    [ValidateSet('FORCEMINIMIZE', 'HIDE', 'MAXIMIZE', 'MINIMIZE', 'RESTORE',
                 'SHOW', 'SHOWDEFAULT', 'SHOWMAXIMIZED', 'SHOWMINIMIZED',
                 'SHOWMINNOACTIVE', 'SHOWNA', 'SHOWNOACTIVATE', 'SHOWNORMAL')]
    [string]$State = 'SHOW'
)

function Set-WindowState {
    param (
        [Parameter(Mandatory = $true)]
        [string]$ProcessName,
        [ValidateSet('FORCEMINIMIZE', 'HIDE', 'MAXIMIZE', 'MINIMIZE', 'RESTORE',
                     'SHOW', 'SHOWDEFAULT', 'SHOWMAXIMIZED', 'SHOWMINIMIZED',
                     'SHOWMINNOACTIVE', 'SHOWNA', 'SHOWNOACTIVATE', 'SHOWNORMAL')]
        [string]$State = 'SHOW'
    )

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

    $Win32ShowWindow = Add-Type -MemberDefinition @'
    [DllImport("user32.dll")]
    public static extern bool ShowWindow(IntPtr hWnd, int nCmdShow);
    [DllImport("user32.dll")]
    public static extern bool SetForegroundWindow(IntPtr hWnd);
'@ -Name "Win32ShowWindow" -Namespace Win32Functions -PassThru

    $ProcessName = $ProcessName -replace '\.exe$', ''
    $processes = Get-Process -Name $ProcessName -ErrorAction SilentlyContinue

    if ($processes) {
        foreach ($proc in $processes) {
            if ($proc.MainWindowHandle -ne 0) {
                $handle = $proc.MainWindowHandle
                $Win32ShowWindow::ShowWindow($handle, $WindowStates[$State]) | Out-Null
                $Win32ShowWindow::SetForegroundWindow($handle) | Out-Null
                Write-Output "SUCCESS: Set window state: $($proc.ProcessName) ($($proc.MainWindowTitle)) -> $State"
                return $true
            }
        }
        Write-Error "No visible window found for process: $ProcessName"
        return $false
    } else {
        Write-Error "Process not found: $ProcessName"
        return $false
    }
}

# 执行函数
Set-WindowState -ProcessName $ProcessName -State $State
