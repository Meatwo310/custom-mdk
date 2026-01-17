# Custom MDK

公式の Forge MDK を拡張し、モダンな Mod 開発向けの追加機能を備えたカスタマイズ版 Minecraft Mod Development Kit (MDK) です。現在は **Minecraft 1.20.1** の **Forge** をサポートしています。

最新版: https://github.com/Meatwo310/custom-mdk/


## ✨ 機能

- 🔄 GitHub Actions による自動ビルド・リリース
- 🧪 セットアップ済み Mixin 環境
- 📛 バージョン・ローダー付き jar 名
- 📚 Parchment マッピング
- ⚙️ Kotlin DSL 移行済みのビルドスクリプト
- 📝 EditorConfig による簡易的なコードスタイル統一
- 📦️ JEI など開発用 Mod セットアップ済み


## 📖 使い方

### 🗂️ セットアップ

1. `Use this template` ボタンをクリックして、新しいリポジトリを作成します。
2. リポジトリをローカルへクローンします。
3. `build.gradle.kts` の `ModConfig` オブジェクトを編集します。
4. `src/main/java/` 内のパッケージ構造を `mod_group_id` に合わせて移動します。
5. `ExampleMod.java` を、 `mod_id` に合わせてリネームします。
6. `examplemod.mixins.json` のファイル名・中身を、 `mod_id` に合わせてリネーム・編集します。
7. `LICENSE` ファイルを好きなライセンスに差し替えます。 MIT ライセンスを使用する場合は、 `LICENSE` ファイルの名義 `Meatwo310` を、ご自身の名前/ハンドルネーム等へ変更します。
8. この `README.md` ファイルを削除するか、 `TEMPLATE-README.md` 等へリネームしてください。

### 🔨 ビルド

```bash
# Mod をビルド
./gradlew build

# 成果物は build/libs/ に生成されます
# フォーマット:  {mod_id}-{minecraft_version}-forge-v{mod_version}.jar
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
