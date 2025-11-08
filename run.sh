#!/bin/bash

nohup mvn exec:java -Dexec.mainClass="ru.svsand.pricer.tgbot.Application" > logs/nohup.out 2> logs/nohup.err &