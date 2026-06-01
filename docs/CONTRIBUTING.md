# Contributing

## Commit Message Convention

Commits should follow [Conventional Commits](https://www.conventionalcommits.org/):

```
type(scope): description
```

Use the smallest scope that describes the affected area.

## Scope Rules

Subprojects are the top-level directories for shared code, Minecraft versions, and loader targets, such as `common`, `1.20.1-common`, `1.20.1-fabric`, `1.20.1-forge`, or `26.1.2-neo`.

| Changes affect...                            | Scope                                                                    |
|----------------------------------------------|--------------------------------------------------------------------------|
| A single subproject                          | Use that directory name, such as `fix(26.1.2-fabric): ...`               |
| Multiple but not all subprojects             | Include the relevant names, such as `feat(1.20.1-forge,1.21.1-neo): ...` |
| All loaders for a specific Minecraft version | Use only the version number, such as `fix(1.20.1): ...`                  |
| All Minecraft versions for a specific loader | Use only the loader name, such as `feat(fabric): ...`                    |
| All subprojects equally                      | Scope is optional                                                        |
| Root-level repository metadata only          | Scope is optional                                                        |

## Recommended Types

`feat` / `fix` / `docs` / `style` / `refactor` / `perf` / `test` / `build` / `ci` / `chore` / `revert` / `release`

Generated release notes include breaking changes (`!`), `feat`, `fix`, and `perf`
commits. Other types are still useful for repository history, but are omitted
from release notes.

## Examples

```
feat(26.1.2-fabric): add config screen support
fix(1.20.1-forge): resolve startup crash
build: update runtime mod staging
ci: add runtime test artifact upload
docs: refresh supported platform matrix
chore(1.18.2-forge,1.19.2-forge): bump forge versions
fix(1.20.1): resolve issue across all 1.20.1 loaders
feat(fabric): add Mod Menu integration across Fabric targets
```

## MDK Template Changes

When committing changes to the MDK template itself, use `mdk` as the type so
that downstream mod release notes can exclude them.

For changes that would otherwise use `feat` or `fix`, use the scope rules above
to identify the affected subprojects, Minecraft version, or loader. For other
changes, use the scope to preserve the usual type, such as `build`, `ci`, or
`docs`.

```
mdk(26.1.2-fabric): add config screen support
mdk(1.20.1-forge): resolve startup crash
mdk(fabric): update Mod Menu integration across Fabric targets
mdk(ci): add release workflow
mdk(build): move the mod version to version.txt
```
