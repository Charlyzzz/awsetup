import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.copyTo
import kotlin.io.path.exists

object Files {
    private const val BACKUP_FILE_EXTENSION = ".bak"

    fun backup(path: Path) {
        val backupFileName = path.fileName.toString() + BACKUP_FILE_EXTENSION
        val backupFile = Path(path.parent.toString(), backupFileName).toFile()
        if (!backupFile.exists()) {
            println("backing up '${path.fileName}' to '$backupFileName'")
            path.toFile().copyTo(backupFile)
        }
    }

    fun restoreBackup(originalPath: Path) {
        val backupPath = Path(originalPath.toString() + BACKUP_FILE_EXTENSION)
        if (backupPath.exists()) {
            val originalFileName = backupPath.fileName.toString().replace(BACKUP_FILE_EXTENSION, "")
            val originalFilePath = Path(backupPath.parent.toString(), originalFileName)
            backupPath.copyTo(originalFilePath, overwrite = true)
            backupPath.toFile().delete()
        } else {
            println("backup file not found, skipping")
        }
    }
}