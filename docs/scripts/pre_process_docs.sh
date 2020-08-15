#!/usr/bin/env bash

set -ex

# Copy the changelog into the site, omitting the unreleased section
cat CHANGELOG.md \
  | sed -e '/^## Unreleased/,/^## /{//!d}' \
  | sed -e '/^## Unreleased/d' \
 > docs/changelog.md

# Copy the README into the index, omitting the license, docs links, and fixing hrefs
cat README.md \
  | sed 's:docs/img:img:g' \
  | grep -wvE '_badge' \
  | sed -e '/^## Documentation/,/^## /{//!d}' \
  | sed -e '/^## Documentation/d' \
  | sed -e '/^## License/,/^\[hack\]/d' \
  > docs/index.md