#!/usr/bin/python

import getopt
import sys
import os
import re
import operator
import math

usage = """
  python <script-name> -b benchmark -s "density submissions" -f <yes|no> "floating point or not" -r<yes|no> "whether to round official data or not"
"""
months = ["2016-01","2016-02","2016-03","2016-04","2016-05","2016-06","2016-07","2016-08","2016-09","2016-10","2016-11","2016-12"]
opts,args = getopt.getopt(sys.argv[1:], "b:s:f:r:")
for o,a in opts:
    if o == "-b":
      benchmark = a
    elif o == "-s":
      submissions = a
    elif o == "-f":
      if a == "yes":
        floating = True
      elif a == "no":
        floating = False
      else:
        print usage
        sys.exit(2)
    elif o == "-r":
      if a == "yes":
        rounding = True
      elif a == "no":
        rounding = False
      else:
        print usage
        sys.exit(2)
    else:
      print usage
      sys.exit()

try:
  benchmark;submissions;floating 
except NameError:
  print usage
  sys.exit()


def substitute(country):
  if country == "EU28":
     return "EU"
  elif country == "EA19":
     return "EA"
  elif country == "EA-18":
     return "EA"
  else:
     return country

def process_benchmark(filepath):
  data = {}
  f = open(filepath, 'r')
  # skip the header line
  lines = f.readlines()[1:]
  f.close()
  for l in lines:
    l = l.strip()
    if len(l) < 1:
      continue
    toks = re.split("\s+", l)
    country = toks[0]
    # check if there is a case where NaNs are followed by numbers and raise an error if this is so
    if "NaN" in toks:
      i = toks.index("NaN")
      tail = toks[i:]
      if len(set(tail)) > 1:
         raise Exception("Consistency error - values after NaNs.")
      toks = toks[:i]
    data[country] = toks[1:]
  return data

# expected submissions in the format
# Country
# PXapproachN n1 n2 .. n12
# 
# ..
def linearize_submissions(filepath, official_series):
  f = open(filepath, 'r')
  # skip header line
  lines = f.readlines()[1:]
  f.close()
  incountry = ""
  inapproach = ""
  for l in lines:
    l = l.strip()
    if len(l) < 1:
      continue
    toks = re.split("\s+", l)
    if len(toks) == 1 and not re.match(r'P\d',toks[0]):
      incountry = toks[0]
      incountry = substitute(incountry)
      continue

    if len(toks) == 1 and re.match(r'P\d',toks[0]):
      continue
        
    if re.match(r'P\d',toks[0]):
      # inapproach now contains the approach and the means of the months
      inapproach = toks
      continue
 
    if toks[0][0] == "(":
      if inapproach == "":
        raise Exception("Unexpected line " + l)
      appr = inapproach[0]
      means = inapproach[1:]
      # remove leading and trailing braces
      stdevs = [x[1:-1] for x in toks] 
      for i in range(0,len(means)):
        if i < len(official_series[incountry]) and rounding:
          print incountry, appr, months[i], round(float(official_series[incountry][i]),1), means[i], stdevs[i]
        if i < len(official_series[incountry]) and not rounding:
          print incountry, appr, months[i], official_series[incountry][i] , means[i], stdevs[i]

if __name__ == "__main__":
  print "Country", "Approach", "Month", "Benchmark", "SubmittedMean", "SubmittedStDev"
  official_series = process_benchmark(benchmark)
  submissions = linearize_submissions(submissions, official_series)
