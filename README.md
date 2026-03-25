# Custom MDK

A customized Minecraft Mod Development Kit (MDK) that extends the official Forge MDK with additional features for modern mod development.

Supported versions:
- [1.20.1 Forge](https://github.com/Meatwo310/custom-mdk/tree/1.20.1-forge)
- [26.1 NeoForge](https://github.com/Meatwo310/custom-mdk/tree/26.1-neo)

Latest version: https://github.com/Meatwo310/custom-mdk/


## ✨ Features

- 🔄 Automated build & release via GitHub Actions
- 🧪 Pre-configured Mixin environment
- 📛 Jar naming with version and loader info
- 📚 Parchment mappings (1.20.1 Forge only)
- ⚙️ Build scripts migrated to Kotlin DSL
- 📝 Unified code style with EditorConfig
- 📦️ Pre-configured development mods (JEI, etc.)


## 📖 Usage

### 🗂️ Setup

1. Click the `Use this template` button to create a new repository.
2. Clone the repository to your local machine.
3. Edit the `ModConfig` object in `build.gradle.kts`.

    | Constant Name           | Description                                                                | Example                                          |
    |:------------------------|:---------------------------------------------------------------------------|:-------------------------------------------------|
    | `MOD_ID`                | Internal name. Used for item/block IDs, etc.                               | `examplemod`                                     |
    | `MOD_NAME`              | Display name. Used in JEI/Jade tooltip, Mod menu, etc.                     | `Example Mod`                                    |
    | `MOD_LICENSE`           | License. Open source licenses like MIT are recommended.                    | `MIT`                                            |
    | `MOD_VERSION`           | Version. Semantic versioning is recommended.                               | `0.1.0`                                          |
    | `MOD_GROUP_ID`          | Group ID. Recommended format is `io.github.<GitHub-Username>.<MOD_ID>`.    | `io.github.meatwo310.examplemod`                 |
    | `MOD_AUTHORS`           | Authors. Comma-separated for multiple authors. Displayed in Mod menu, etc. | `Meatwo310`                                      |
    | `MOD_DESCRIPTION`       | Description. Can be multi-line. Displayed in Mod menu, etc.                | `An example mod.`                                |
    | `MOD_DISPLAY_URL`       | Homepage URL. Displayed in Mod menu, etc.                                  | `https://github.com/Meatwo310/custom-mdk`        |
    | `MOD_ISSUE_TRACKER_URL` | Issue tracker URL. Displayed in Mod menu, etc.                             | `https://github.com/Meatwo310/custom-mdk/issues` |

4. Move the package structure in `src/main/java/` to match `MOD_GROUP_ID`.
5. Rename `ExampleMod.java` to match `MOD_ID`.
6. Rename and edit `examplemod.mixins.json` (both filename and contents) to match `MOD_ID`.
7. Replace the `LICENSE` file with your preferred license. If using MIT License, change the name `Meatwo310` in the `LICENSE` file to your own name/handle.
8. Delete this `README.md` file or rename it to something like `TEMPLATE-README.md`.

### 🔨 Build

```bash
# Build the mod
./gradlew build

# Output will be in build/libs/
# Format: {mod_id}-{minecraft_version}-{loader}-v{mod_version}.jar
```
> [!TIP]
> Commits pushed to GitHub are automatically built by GitHub Actions and uploaded as artifacts.

### 🐞 Running Development Environment

```bash
# Run client
./gradlew runClient

# Run server
./gradlew setupServer  # Agree to EULA and generate server.properties
./gradlew runServer

# Run data generation
./gradlew runData
```

### 🚀 Release
You can create a release by pushing a tag.
```bash
# Create and push a tag for version 1.20.1-forge-v0.1.0
git tag 1.20.1-forge-v0.1.0
git push origin 1.20.1-forge-v0.1.0
```


## ⚖️ License

This template is published under the MIT License. See the `LICENSE` file for details.

For projects generated from this template, Meatwo310 does not enforce any license. (Open source licenses are recommended!)

> [!WARNING]
> Don't forget to change the license name to your own!


## 🤝 Contributing

Don't like the template? Feel free to fork and customize it! I'll happily incorporate useful improvements 👀

Issues and pull requests are welcome!

---

# 🇯🇵日本語版

公式の Forge MDK を拡張し、モダンな Mod 開発向けの追加機能を備えたカスタマイズ版 Minecraft Mod Development Kit (MDK) です。現在は **Minecraft 1.20.1** の **Forge** をサポートしています。

最新版: https://github.com/Meatwo310/custom-mdk/


## ✨ 機能

- 🔄 GitHub Actions による自動ビルド・リリース
- 🧪 セットアップ済み Mixin 環境
- 📛 バージョン・ローダー付き jar 名
- 📚 Parchment マッピング (1.20.1 Forge のみ)
- ⚙️ Kotlin DSL 移行済みのビルドスクリプト
- 📝 EditorConfig による簡易的なコードスタイル統一
- 📦️ JEI など開発用 Mod セットアップ済み


## 📖 使い方

### 🗂️ セットアップ

1. `Use this template` ボタンをクリックして、新しいリポジトリを作成します。
2. リポジトリをローカルへクローンします。
3. `build.gradle.kts` の `ModConfig` オブジェクトを編集します。

    | 定数名                     | 説明                                               | 例                                                |
    |:------------------------|:-------------------------------------------------|:-------------------------------------------------|
    | `MOD_ID`                | 内部的な名前。アイテムやブロックの ID 等にも使用される。                   | `examplemod`                                     |
    | `MOD_NAME`              | 表示名。 JEI/Jade の Mod 名表示や、 Mod メニュー等に使用される。       | `Example Mod`                                    |
    | `MOD_LICENSE`           | ライセンス。 MIT などオープンソースライセンスを推奨。                    | `MIT`                                            |
    | `MOD_VERSION`           | バージョン。セマンティックバージョニングを推奨。                         | `0.1.0`                                          |
    | `MOD_GROUP_ID`          | グループ ID。 `io.github.<GitHubユーザー名>.<MOD_ID>` を推奨。 | `io.github.meatwo310.examplemod`                 |
    | `MOD_AUTHORS`           | 作者名。コンマ区切りで複数人を指定可能。 Mod メニュー等で表示。               | `Meatwo310`                                      |
    | `MOD_DESCRIPTION`       | 説明。複数行設定可能。 Mod メニュー等で表示。                        | `An example mod.`                                |
    | `MOD_DISPLAY_URL`       | ホームページ。 Mod メニュー等で表示。                            | `https://github.com/Meatwo310/custom-mdk`        |
    | `MOD_ISSUE_TRACKER_URL` | 不具合報告用 URL。 Mod メニュー等で表示。                        | `https://github.com/Meatwo310/custom-mdk/issues` |
    | `MOD_CREDITS`           | クレジット欄。 Mod メニュー等で表示。                            | `Special thanks to the original MDK!`            |

4. `src/main/java/` 内のパッケージ構造を `MOD_GROUP_ID` に合わせて移動します。
5. `ExampleMod.java` を、 `MOD_ID` に合わせてリネームします。
6. `examplemod.mixins.json` のファイル名・中身を、 `MOD_ID` に合わせてリネーム・編集します。
7. `LICENSE` ファイルを好きなライセンスに差し替えます。 MIT ライセンスを使用する場合は、 `LICENSE` ファイルの名義 `Meatwo310` を、ご自身の名前/ハンドルネーム等へ変更します。
8. この `README.md` ファイルを削除するか、 `TEMPLATE-README.md` 等へリネームしてください。

### 🔨 ビルド

```bash
# Mod をビルド
./gradlew build

# 成果物は build/libs/ に生成されます
# フォーマット:  {mod_id}-{minecraft_version}-{mod_loader}-v{mod_version}.jar
```
> [!TIP]
> GitHub にプッシュされたコミットは、自動的に GitHub Actions でビルドされ、アーティファクトとしてアップロードされます。

### 🐞 開発環境の起動

```bash
# クライアントを実行
./gradlew runClient

# サーバーを実行
./gradlew setupServer  # EULA に同意し、 server.properties を生成
./gradlew runServer

# Data Genを実行
./gradlew runData
```

### 🚀 リリース
タグをプッシュすると、リリースを作成できます。
```bash
# バージョン 1.20.1-forge-v0.1.0 用のタグを作成し、プッシュ
git tag 1.20.1-forge-v0.1.0
git push origin 1.20.1-forge-v0.1.0
```


## ⚖️ ライセンスについて

このテンプレートは MIT ライセンスの下に公開されています。詳細は `LICENSE` ファイルを参照してください。

テンプレートから生成されたプロジェクトについて、 Meatwo310 はライセンスを強制しません。 (オープンソースライセンスを推奨します！)

> [!WARNING]
> ライセンスの名義をご自身の名前へ変更することを忘れないでください！


## 🤝 貢献

テンプレートが気に入りませんか？ ぜひフォークし、カスタムしてお使いください！ 有用な改善点は勝手に取り込みます 👀

イシュー・プルリクエストも歓迎です！
