# hawk.scala

Scala errors Catcher module for [Hawk.so](https://hawk.so)

## Usage

[Register](https://hawk.so/join) an account and get a project token.

### Install module

```bash
$ sbt build
```

#### Download and require codex.hawk package

You can download this repository and import `codex.hawk.HawkCatcher` class in your project.

```scala
import codex.hawk.HawkCatcher
```

### Init HawkCatcher

Create an instance with token to the entry point of your project.

```scala
val hawkCatcher = new HawkCatcher("<token>")
```

### Catch exception

You can catch exceptions by yourself without enabling handlers.

```scala
try {
  throw new Exception("Major error")
} catch {
  case e: Exception => new HawkCatcher("<token>").catchException(e)
}
```

## Links

Repository: https://github.com/codex-team/hawk.scala

Report a bug: https://github.com/codex-team/hawk.scala/issues

CodeX Team: https://ifmo.su