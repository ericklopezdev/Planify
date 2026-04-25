#!/bin/bash
# reload-app.sh
# Builds the debug APK and installs it on the connected device.
# Requires USB debugging enabled on the phone.

set -e

GREEN='\033[0;32m'; RED='\033[0;31m'; CYAN='\033[0;36m'; BOLD='\033[1m'; NC='\033[0m'
ok()   { echo -e "${GREEN}✔${NC}  $1"; }
fail() { echo -e "${RED}✘${NC}  $1"; exit 1; }
step() { echo -e "\n${BOLD}── $1${NC}"; }

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
ADB="${ANDROID_HOME:-$HOME/Android/sdk}/platform-tools/adb"

# ── Check ADB ─────────────────────────────────────────────────────────────────
[[ -x "$ADB" ]] || fail "ADB not found at $ADB. Run .scripts/setup-android-linux.sh first."

# ── Check device ──────────────────────────────────────────────────────────────
step "Device"

DEVICES=$("$ADB" devices | grep "device$" | awk '{print $1}')
DEVICE_COUNT=$(echo "$DEVICES" | grep -c . || true)

if [[ "$DEVICE_COUNT" -eq 0 ]]; then
    fail "No device found.\n\n  1. Connect your phone via USB\n  2. Enable USB debugging: Settings → About phone → tap Build number 7 times\n     then Settings → Developer options → USB debugging\n  3. Accept the prompt on your phone"
fi

if [[ "$DEVICE_COUNT" -gt 1 ]]; then
    echo "Multiple devices connected:"
    echo "$DEVICES" | nl -w2
    echo -e "${CYAN}Pick a number: ${NC}\c"; read -r n
    DEVICE=$(echo "$DEVICES" | sed -n "${n}p")
    ADB="$ADB -s $DEVICE"
fi

MODEL=$($ADB shell getprop ro.product.model 2>/dev/null | tr -d '\r')
ok "Device: $MODEL"

# ── Build + install ───────────────────────────────────────────────────────────
step "Build & install"

cd "$PROJECT_ROOT"
[[ -f "gradlew" ]] || fail "gradlew not found in $PROJECT_ROOT"
chmod +x gradlew

# installDebug = assembleDebug + adb install in one step
if ! ./gradlew installDebug 2>&1; then
    # Signature mismatch — uninstall and retry once
    warn "Install failed. Trying uninstall + reinstall..."
    $ADB uninstall "com.app.planify" 2>/dev/null || true
    ./gradlew installDebug
fi

echo ""
echo -e "${GREEN}${BOLD}Done — app installed on $MODEL${NC}"
echo ""
