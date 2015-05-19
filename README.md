# githubsearchcli
**githubsearchcli** is a command line tool for executing searches against public Git Hub repositories.
Search results are displayed to standard output.

## Requirements
**githubsearchcli** requires Java 7 or higher.

## Download
The githubsearchcli-1.0.jar executable jar file is located in the [jlyoung/githubsearchcli/target](https://github.com/jlyoung/githubsearchcli/tree/master/target) folder.
You can clone the project locally:
```
git clone https://jlyoung@github.com/jlyoung/githubsearchcli.git
```
You can also download the jar file directly from [here](https://github.com/jlyoung/githubsearchcli/blob/master/target/githubsearchcli-1.0.jar?raw=true).

## Usage
```
[user@localhost] $ java -jar githubsearchcli-1.0.jar --help
usage: java -jar githubsearchcli-1.0.jar [-f <FIELD1 FIELD2 FIELD3>] [-h]
       [-l <NUM>] -s <"SEARCH TERMS">
Search Git Hub from the command line

 -f,--fields <FIELD1 FIELD2 FIELD3>   Fields to be included in search
                                      Valid search fields: name
                                      description readme
 -h,--help                            This help message
 -l,--limit <NUM>                     Number of entries to limit search
                                      results to
 -s,--search-term <"SEARCH TERMS">    Terms to search for

Author: Joe Young http://joeyoung.io
```

## Example
```
[user@localhost] $ java -jar githubsearchcli-1.0.jar -s "commons cli" -l 5 -f name description readme
Displaying 5 of 1379 results:

apache/commons-cli                                                Java * 33    
Mirror of Apache Commons CLI
Updated on 2015/05/11
Repo: https://github.com/apache/commons-cli
git clone https://github.com/apache/commons-cli.git
--------------------------------------------------------------------------------
rest-client/rest-client                                           Ruby * 2701  
Simple HTTP and REST client for Ruby, inspired by microframework syntax for specifying actions.
Updated on 2015/05/19
Repo: https://github.com/rest-client/rest-client
git clone https://github.com/rest-client/rest-client.git
--------------------------------------------------------------------------------
dfhoughton/CLI                                                    Java * 2     
Very minimal, declarative CLI parsing library for Java
Updated on 2014/04/20
Repo: https://github.com/dfhoughton/CLI
git clone https://github.com/dfhoughton/CLI.git
--------------------------------------------------------------------------------
mediashelf/fedora-client                                          Java * 12    
Java client library for the Fedora Commons Digital Repository
Updated on 2015/03/15
Repo: https://github.com/mediashelf/fedora-client
git clone https://github.com/mediashelf/fedora-client.git
--------------------------------------------------------------------------------
NetCommons3/NetCommons3                                            PHP * 13    

Updated on 2015/05/07
Repo: https://github.com/NetCommons3/NetCommons3
git clone https://github.com/NetCommons3/NetCommons3.git
--------------------------------------------------------------------------------
```
