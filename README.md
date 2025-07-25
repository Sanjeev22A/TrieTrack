

# TrieTrack

**TrieTrack** is a lightweight local version control system (VCS) built in Java for efficient line-level tracking of file changes. It features a custom GUI text editor and hybrid storage support via **MongoDB** or **file serialization**, allowing users to manage file history, view diffs, and push selected versions to GitHub without relying on external VCS tools.

---

## 🔍 Features

* ✅ **Line-Level Change Tracking** using a Trie-based structure
* 📝 **Integrated Java Swing Text Editor** for seamless interaction
* 📦 **Hybrid Storage**:

  * **MongoDB** for structured persistence
  * **Serialized Files** for offline usage
* 🧭 **Efficient Version Navigation** with rollback, view history, and comparison
* ☁️ **GitHub Push Support** for remote versioning
* 🔒 **User-Specific Environment Settings**
* 📂 Modular Codebase with clear packages (`GUI`, `Database`, `Service`, `ENV`, etc.)

---

## 📁 Project Structure

```
src/main/
│
├── GUI/              # Swing-based Editor and GUI handling
├── Database/         # MongoDB or file-based version storage
├── Service/          # Core logic for Trie, versioning, diffing
├── ENV/              # Environment setup and user configs
├── Testpackage/      # Unit and integration tests
└── org/example/      # Main application entry and helper classes
```

---

## 🧠 Core Concepts

### 🔡 Trie-Based Versioning

Each file’s version is stored using a Trie structure that maps line-level changes efficiently. This allows:

* Fast retrieval of diffs
* Minimal storage footprint
* Hierarchical version traceability

### 🧰 Text Editor

A lightweight Java Swing editor built from scratch with:

* Save, revert, version switch features
* Syntax highlighting support (extendable)
* Integrated version visualization

### ☁️ Hybrid Storage Options

* **MongoDB**: Ideal for structured, queryable version data
* **Serialized Files**: For offline use or portable deployment

---

## 🚀 Getting Started

### Prerequisites

* Java 11+
* Maven
* [MongoDB](https://www.mongodb.com/) (optional, for DB storage mode)

### Clone the Repository

```bash
git clone https://github.com/Sanjeev22A/TrieTrack.git
cd TrieTrack
```

### Build and Run

```bash
mvn clean install
java -jar target/TrieTrack.jar
```

---

## 🛠️ Usage

1. Launch the editor (`TrieTrack.jar`)
2. Open a file or create a new one.
3. Save versions locally.
4. Use GUI buttons to:

   * View version history
   * Switch versions
   * Push to GitHub (requires token setup)
5. Configure Mongo/File mode via `ENV/UserSettings.java`

---

## 🐛 Known Limitations

* Only tracks plain text files (future support for other formats planned)
* GUI push to GitHub requires GitHub token pre-setup in ENV
* No branching support (linear history)

---

## 📌 To-Do

* [ ] Syntax highlighting support
* [ ] Branching and merge support
* [ ] GitHub authentication via OAuth
* [ ] Conflict resolution UI

---

## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

---

## 📄 License

MIT License. See `LICENSE` file for details.

---

## 👤 Author

Developed as part of an **OOAD Final Project** by [Sanjeev22A](https://github.com/Sanjeev22A).


