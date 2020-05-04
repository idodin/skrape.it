## Experiments

This fork of the original skrape.it project contains experiments on improving the Release Pipeline for the system, as part of a Release Engineering course I took at McGill University.

Please find ruby scripts for experimentation in the experiment directory, Dockerfiles are provided for your ease of use. We experimented with:
* Gradle vs Maven - improvements on build time with and without caching
* Travis vs Github Actions - improvements on time to release


<img width="150px" height="150px" align="right" src="skrape.png"/>

[![Documentation](https://img.shields.io/badge/skrape%7Bit%7D-docs-blue.svg)](https://docs.skrape.it)
[![maven central](https://img.shields.io/maven-central/v/it.skrape/skrapeit-core.svg?color=0)](https://search.maven.org/search?q=g:it.skrape%20AND%20a:skrapeit-core&skrapeit-core=gav)
[![License](https://img.shields.io/github/license/skrapeit/skrape.it.svg)](https://github.com/skrapeit/skrape.it/blob/master/LICENSE)
[![JDK](https://img.shields.io/badge/jdk-8-green.svg)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

[![master build status](https://img.shields.io/travis/skrapeit/skrape.it.svg?label=master)](https://travis-ci.org/skrapeit/skrape.it)
[![Codecov](https://img.shields.io/codecov/c/github/skrapeit/skrape.it.svg)](https://codecov.io/gh/skrapeit/skrape.it)
[![Known Vulnerabilities](https://snyk.io/test/github/skrapeit/skrape.it/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/skrapeit/skrape.it?targetFile=pom.xml)

[![contributions welcome](https://img.shields.io/badge/contributions-welcome-brightgreen.svg?style=flat)](https://github.com/skrapeit/skrape.it/issues)
[![Donate](https://img.shields.io/badge/-donate-blue.svg?logo=paypal)](https://www.paypal.me/skrapeit)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://kotlin.link/?q=Testing)

[skrape{it}](https://docs.skrape.it)
====================================

_**[skrape{it}](http://www.skrape.it)**_ is a Kotlin-based HTML/XML testing and web scraping library
that can be used seamlessly in Spring-Boot, Ktor, Android or other Kotlin-JVM projects.
The ability to analyze and extract HTML including client-side rendered DOM trees and all other XML-related markup specifications such as SVG, UML, RSS,... makes it unique.
It places particular emphasis on ease of use and a high level of readability by providing an intuitive DSL.
First and foremost skrape{it} aims to be a testing tool (not tied to a particular test runner), but it can also be used to scrape websites in a convenient fashion.

#### Extensions
In addition, extensions for known testing libraries are provided to extend them with the mentioned skrape{it} functionality.
Currently available:
* [Skrape{it} MockMvc extension](https://github.com/skrapeit/skrapeit-mockmvc-extension)
* [Skrape{it} Ktor extension](https://github.com/skrapeit/skrapeit-ktor-extension)

### Read the Docs
You'll always find latest documentation, release notes and examples at 
**[https://docs.skrape.it](https://docs.skrape.it)**
