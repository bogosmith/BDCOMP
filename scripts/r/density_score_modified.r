#!/usr/bin/Rscript

# This script produces the log of the likelihood of each benchmark given the respective forecast. The script is pretty quick and dirty.

args <- commandArgs(TRUE)
input_file <- args[1]
output_file <- args[2]
if (file.exists(output_file)) {
 quit(save="no",status=2)
}

table <- read.table(input_file, header=TRUE)
table["standscore"] <- (table$Benchmark - table$SubmittedMean) / table$SubmittedStDev
table["score"] <- dnorm(table$standscore*table$SubmittedStDev/table$SubmittedMean + 1, 1, table$SubmittedStDev/table$SubmittedMean)
table["LogScore"] <- log(table$score)
table$standscore <- NULL
write.table(table[, !(names(table) %in% "standscore")], file=output_file, row.names=FALSE)
