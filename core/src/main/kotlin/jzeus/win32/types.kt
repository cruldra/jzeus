package jzeus.win32

enum class WindowState(val value: Int) {
    FORCEMINIMIZE(11),   // 强制最小化窗口，即使拥有窗口的线程被挂起
    HIDE(0),             // 隐藏窗口
    MAXIMIZE(3),         // 最大化窗口
    MINIMIZE(6),         // 最小化窗口
    RESTORE(9),          // 恢复窗口原来的位置和大小
    SHOW(5),            // 显示窗口
    SHOWDEFAULT(10),     // 根据程序启动信息显示窗口
    SHOWMAXIMIZED(3),    // 激活并最大化窗口
    SHOWMINIMIZED(2),    // 激活并最小化窗口
    SHOWMINNOACTIVE(7),  // 最小化窗口但不激活
    SHOWNA(8),          // 以当前状态显示窗口但不激活
    SHOWNOACTIVATE(4),   // 以最近的大小和位置显示窗口但不激活
    SHOWNORMAL(1);       // 激活并显示为正常状态

    companion object {
        // 通过值查找枚举
        fun fromValue(value: Int): WindowState? = values().find { it.value == value }

        // 通过名称查找枚举（忽略大小写）
        fun fromString(name: String): WindowState? = values().find {
            it.name.equals(name, ignoreCase = true)
        }
    }

    // 添加一些使用示例的扩展函数
    fun isMinimized(): Boolean = this in listOf(MINIMIZE, SHOWMINIMIZED, SHOWMINNOACTIVE)
    fun isMaximized(): Boolean = this in listOf(MAXIMIZE, SHOWMAXIMIZED)
    fun isHidden(): Boolean = this == HIDE
    fun isVisible(): Boolean = this != HIDE
    fun isActive(): Boolean = this !in listOf(SHOWMINNOACTIVE, SHOWNA, SHOWNOACTIVATE)
}
