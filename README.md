# JULA-Async-Connector
***JU**nit **L**LM **A**utomator*

This repository is part of a Proof of Concept (PoC) project aimed at automating JUnit-based test generation using a locally hosted Large Language Model (LLM) server (Ollama).<br><br>
Other components:

- [JULA-GitHubActions-Agent](https://github.com/MarshallBaby/JULA-GitHubActions-Agent)
- [JULA-Worker](https://github.com/MarshallBaby/JULA-Worker)

## Async Connector
This service facilitates communication between the GitHub Actions worker and the locally hosted LLM server.

### Workflow:
1. **JULA-GitHubActions-Agent** posts a task (LLM query).
2. The task is saved with the status **WAITING**.
3. **JULA-Worker** fetches the task, updating its status to **IN_PROGRESS**.
4. **JULA-Worker** processes the task and updates its status to **COMPLETE**.
5. **JULA-GitHubActions-Agent** retrieves the response, and the task is removed from the database.