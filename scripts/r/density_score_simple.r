#!/usr/bin/Rscript

# This script produces the log of the likelihood of each benchmark given the respective forecast. The script is pretty quick and dirty.

args <- commandArgs(TRUE)
input_file <- args[1]
output_file <- args[2]
if (file.exists(output_file)) {
 quit(save="no",status=2)
}

table <- read.table(input_file, header=TRUE)
table["score"] <- dnorm(table$Benchmark, table$SubmittedMean, table$SubmittedStDev)
table["LogScore"] <- log(table$score)
write.table(table, file=output_file, row.names=FALSE)
