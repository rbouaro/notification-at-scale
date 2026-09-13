#!/usr/bin/env bash
# Installs project git hooks from scripts/hooks/ into .git/hooks/.
# Run this once after cloning the repository.

set -euo pipefail

REPO_ROOT="$(git rev-parse --show-toplevel)"
HOOKS_SOURCE="$REPO_ROOT/scripts/hooks"
HOOKS_TARGET="$REPO_ROOT/.git/hooks"

if [ ! -d "$HOOKS_SOURCE" ]; then
  echo "Error: hooks source directory not found: $HOOKS_SOURCE" >&2
  exit 1
fi

installed=0

for hook in "$HOOKS_SOURCE"/*; do
  name="$(basename "$hook")"
  target="$HOOKS_TARGET/$name"

  if [ -e "$target" ] && [ ! -L "$target" ]; then
    echo "Skipping $name — a non-symlink hook already exists at $target"
    continue
  fi

  ln -sf "$hook" "$target"
  chmod +x "$hook"
  echo "Installed $name"
  installed=$((installed + 1))
done

echo ""
echo "$installed hook(s) installed."
