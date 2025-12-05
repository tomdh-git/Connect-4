using System;
using System.Diagnostics;
using System.IO;
using System.Reflection;

class Launcher {
    static void Main(string[] args) {
        string tempPath = Path.Combine(Path.GetTempPath(), "Connect-4-" + Guid.NewGuid().ToString() + ".jar");
        try {
            // Extract resource
            // The resource name matches the filename used in /resource:filename
            using (Stream stream = Assembly.GetExecutingAssembly().GetManifestResourceStream("Connect-4.jar")) {
                if (stream == null) {
                    Console.WriteLine("Error: Could not find embedded JAR resource.");
                    Console.WriteLine("Available resources:");
                    foreach (string name in Assembly.GetExecutingAssembly().GetManifestResourceNames()) {
                        Console.WriteLine(" - " + name);
                    }
                    Console.ReadKey();
                    return;
                }
                using (FileStream fileStream = new FileStream(tempPath, FileMode.Create)) {
                    stream.CopyTo(fileStream);
                }
            }

            // Run Java
            ProcessStartInfo startInfo = new ProcessStartInfo();
            startInfo.FileName = "java";
            startInfo.Arguments = "-jar \"" + tempPath + "\"";
            startInfo.UseShellExecute = false; // Share the console
            
            Process process = Process.Start(startInfo);
            process.WaitForExit();
        } catch (Exception e) {
            Console.WriteLine("Error launching game: " + e.Message);
            Console.WriteLine("Make sure Java is installed and in your PATH.");
            Console.WriteLine("Press any key to exit...");
            Console.ReadKey();
        } finally {
            // Cleanup
            if (File.Exists(tempPath)) {
                try { File.Delete(tempPath); } catch { }
            }
        }
    }
}
