#!/usr/bin/python

import getopt
import sys
import os
import re

usage = """
  python <script-name> -b benchmark -s submissions
"""
opts,args = getopt.getopt(sys.argv[1:], "b:s:")
for o,a in opts:
    if o == "-b":
      benchmark = a
    elif o == "-s":
      submissions = a
    else:
      print usage
      sys.exit()
try:
  benchmark;submissions; 
except NameError:
  print usage
  sys.exit()

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
         raise Error("Consistency error - values after NaNs.")
      toks = toks[:i]
    data[country] = toks[1:]
  return data

# expected submissions in the format
# Country
# PXapproachN n1 n2 .. n12
# ..
def process_submissions(filepath, official_series):
  scores = {}
  f = open(filepath, 'r')
  lines = f.readlines()
  f.close()
  incountry = ""
  for l in lines:
    if len(l) < 1:
      continue
    toks = re.split("\s+", l)
    if len(toks == 1) and not re.match(r'P\d',toks[0]):
      incountry = toks[0]
      continue

    if len(toks == 1) and re.match(r'P\d',toks[0]):
      continue
    
    if not re.match(r'P\dApproach\d\d',toks[0]):
      raise Error("Unexpected line " + l)
    

if __name__ == "__main__":
  official_series = process_benchmark(benchmark)
  print official_series
  
