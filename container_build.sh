#!/bin/bash
# 自动重新编译所有基座需要用到的容器
# @author <a href="https://github.com/limou3434">limou3434</a>

# 开启出错即止
set -e

# 公司名称
COMPANYI='work'

# 定义颜色
GREEN='\033[0;32m[info]'  # 提示
RED='\033[0;31m[erro]'    # 错误
YELLOW='\033[1;33m[wrin]' # 警告
END='\033[0m'             # 重置

# 重要交互
confirm_input() {
    # 读取用户输入
    echo -e "${YELLOW} 该操作比较重要，需要您手动检查，是否继续操作?（使用 “y” 或 “回车” 确认，其他字符均视为 “取消”） ${END}"
    read -r choice
    
    # 判断是否继续执行
    if [ -z "$choice" ] || [ "$choice" = "y" ]; then
        echo -e "${GREEN} 继续执行 ${END}"
        return 0 # 返回继续执行的判断
    else
        echo -e "${RED} 取消执行，已经停止项目的部分启动过程！ ${END}"
        return 1 # 返回取消执行的判断
    fi
}

# TODO：等待完善
