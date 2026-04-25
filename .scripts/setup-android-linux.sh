#!/bin/bash
# setup-android-linux.sh
# Sets up the Android SDK on Linux for terminal-only development.
# Safe to run multiple times.

set -e

GREEN='\033[0;32m'; YELLOW='\033[1;33m'; RED='\033[0;31m'; CYAN='\033[0;36m'; BOLD='\033[1m'; NC='\033[0m'
ok()   { echo -e "${GREEN}✔${NC}  $1"; }
info() { echo -e "${CYAN}→${NC}  $1"; }
warn() { echo -e "${YELLOW}⚠${NC}  $1"; }
fail() { echo -e "${RED}✘${NC}  $1"; exit 1; }
step() { echo -e "\n${BOLD}── $1${NC}"; }

ANDROID_SDK_DIR="$HOME/Android/sdk"
CMDLINE_TOOLS_DIR="$ANDROID_SDK_DIR/cmdline-tools/latest"
SDKMANAGER="$CMDLINE_TOOLS_DIR/bin/sdkmanager"
# Check latest at: https://developer.android.com/studio#command-line-tools-only
CMDLINE_TOOLS_URL="https://dl.google.com/android/repository/commandlinetools-linux-12266719_latest.zip"

SDK_COMPONENTS=(
    "platform-tools"
    "build-tools;35.0.0"
    "platforms;android-36"
)

# ── 1. Java ───────────────────────────────────────────────────────────────────
step "1. Java"

if ! command -v java &>/dev/null; then
    fail "Java not found. Install JDK 17+:\n   sudo dnf install java-21-openjdk-devel   # Fedora\n   sudo apt install openjdk-21-jdk          # Ubuntu"
fi

JAVA_VER=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
[[ "$JAVA_VER" -lt 17 ]] && fail "Java 17+ required, found: $JAVA_VER"
ok "Java $JAVA_VER — $(which java)"

# ── 2. sdkmanager ─────────────────────────────────────────────────────────────
step "2. sdkmanager"

if [[ -x "$SDKMANAGER" ]]; then
    ok "sdkmanager already installed"
else
    warn "sdkmanager not found — downloading command-line tools (~130 MB)..."

    command -v curl &>/dev/null || command -v wget &>/dev/null || \
        fail "curl or wget required. Install one:\n   sudo dnf install curl"

    ZIP="/tmp/android-cmdline-tools.zip"
    if command -v curl &>/dev/null; then
        curl -L --progress-bar "$CMDLINE_TOOLS_URL" -o "$ZIP"
    else
        wget -q --show-progress "$CMDLINE_TOOLS_URL" -O "$ZIP"
    fi

    mkdir -p "$CMDLINE_TOOLS_DIR"
    unzip -q "$ZIP" -d "/tmp/pl-sdk-extract"

    SRC="/tmp/pl-sdk-extract/cmdline-tools"
    [[ -d "$SRC" ]] && cp -r "$SRC"/. "$CMDLINE_TOOLS_DIR/" || cp -r /tmp/pl-sdk-extract/. "$CMDLINE_TOOLS_DIR/"
    rm -rf "$ZIP" "/tmp/pl-sdk-extract"
    ok "sdkmanager installed"
fi

# ── 3. Environment variables ──────────────────────────────────────────────────
step "3. Environment variables"

if [[ "$SHELL" == *"zsh"* ]]; then RC="$HOME/.zshrc"; else RC="$HOME/.bashrc"; fi

if ! grep -q "ANDROID_HOME" "$RC" 2>/dev/null; then
    cat >> "$RC" <<'EOF'

# Android SDK
export ANDROID_HOME="$HOME/Android/sdk"
export PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools"
EOF
    ok "Added ANDROID_HOME to $RC"
    warn "Run 'source $RC' or open a new terminal to apply"
else
    ok "ANDROID_HOME already in $RC"
fi

export ANDROID_HOME="$ANDROID_SDK_DIR"
export PATH="$PATH:$CMDLINE_TOOLS_DIR/bin:$ANDROID_SDK_DIR/platform-tools"

# ── 4. SDK licenses ───────────────────────────────────────────────────────────
step "4. SDK licenses"

if [[ ! -f "$ANDROID_SDK_DIR/licenses/android-sdk-license" ]]; then
    yes | "$SDKMANAGER" --licenses > /dev/null 2>&1 || true
    ok "Licenses accepted"
else
    ok "Licenses already accepted"
fi

# ── 5. SDK components ─────────────────────────────────────────────────────────
step "5. SDK components"

for component in "${SDK_COMPONENTS[@]}"; do
    if "$SDKMANAGER" --list_installed 2>/dev/null | grep -q "^  $component "; then
        ok "$component already installed"
    else
        info "Installing $component..."
        "$SDKMANAGER" "$component"
        ok "$component installed"
    fi
done

# ── 6. Gradle wrapper ────────────────────────────────────────────────────────
step "6. Gradle wrapper"

if [[ -f "$PROJECT_ROOT/gradlew" ]]; then
    ok "gradlew already exists"
else
    GRADLE_VERSION="9.4.1"
    GRADLE_ZIP="/tmp/gradle-bin.zip"
    info "Downloading Gradle $GRADLE_VERSION (~130 MB)..."
    curl -L --progress-bar "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" -o "$GRADLE_ZIP"
    unzip -q "$GRADLE_ZIP" -d /tmp/
    /tmp/gradle-${GRADLE_VERSION}/bin/gradle wrapper --gradle-version "$GRADLE_VERSION" -p "$PROJECT_ROOT"
    rm -rf "$GRADLE_ZIP" "/tmp/gradle-${GRADLE_VERSION}"
    ok "gradlew generated (Gradle $GRADLE_VERSION)"
fi

# ── Done ──────────────────────────────────────────────────────────────────────
ADB="$ANDROID_SDK_DIR/platform-tools/adb"
echo ""
echo -e "${GREEN}${BOLD}All done.${NC}"
echo -e "  SDK:  $ANDROID_SDK_DIR"
echo -e "  ADB:  $("$ADB" version 2>/dev/null | head -1)"
echo ""
echo -e "Next: connect your phone with USB debugging on, then run:"
echo -e "  ${CYAN}.scripts/reload-app.sh${NC}"
echo ""
