# SubstitutionsGenerator
This project generates substitutions.json, necessary to correctly replace
LWJGL and other libraries with natives.
## Usage
```shell
./gradlew run
```
Copy the resulting substitutions.json into the MojoLauncher assets directory.
## Custom versions
Custom version JSONs can be added into extra_versions, and they will be processed like any other.
For some custom versions, additional handling may be necessary. If you plan on adding support for a custom version, make sure to run the generator and test the resulting file in MojoLauncher first.
## TODO
- [ ] Proper data-driven configuration (maybe)
- [ ] Multithreading
- [ ] JSON compaction (maybe)