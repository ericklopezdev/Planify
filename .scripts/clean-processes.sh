#!/bin/bash
# clean-processes.sh
# Stops lingering Gradle daemons, Kotlin compiler daemons, and ADB server.

GREEN='\033[0;32m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; BOLD='\033[1m'; NC='\033[0m'
ok()   { echo -e "${GREEN}✔${NC}  $1"; }
none() { echo -e "${YELLOW}–${NC}  $1"; }
step() { echo -e "\n${BOLD}── $1${NC}"; }

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
ADB="${ANDROID_HOME:-$HOME/Android/sdk}/platform-tools/adb"

# ── Gradle daemons ────────────────────────────────────────────────────────────
step "Gradle daemons"

if [[ -f "$PROJECT_ROOT/gradlew" ]]; then
    cd "$PROJECT_ROOT"
    ./gradlew --stop --quiet 2>/dev/null && ok "Gradle daemons stopped" || none "No active Gradle daemons"
else
    none "gradlew not found, skipping"
fi

# ── Kotlin compiler daemon ────────────────────────────────────────────────────
step "Kotlin compiler daemon"

KT_PIDS=$(pgrep -f "KotlinCompileDaemon" 2>/dev/null || true)
if [[ -n "$KT_PIDS" ]]; then
    kill $KT_PIDS 2>/dev/null && ok "Kotlin daemon killed (PID: $KT_PIDS)"
else
    none "No Kotlin daemon running"
fi

# ── ADB server ────────────────────────────────────────────────────────────────
step "ADB server"

if [[ -x "$ADB" ]]; then
    "$ADB" kill-server 2>/dev/null && ok "ADB server stopped" || none "ADB server was not running"
else
    none "ADB not found, skipping"
fi

# ── Summary ───────────────────────────────────────────────────────────────────
echo ""
echo -e "${GREEN}${BOLD}Done.${NC} All Android dev processes cleaned up."
echo ""
