set %JAVA_HOME%="C:\Program Files\jdk-16\"
"C:\Program Files\jdk-16\bin\jpackage" --input target/lib-maven/ --input target/   --name Empty3ModellerApp  --main-jar empty3-library-3d-gui-2021.4.3-SNAPSHOT.jar --main-class one.empty3.gui.Main --type app-image   --java-options '--enable-preview'
