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
         raise Exception("Consistency error - values after NaNs.")
      toks = toks[:i]
    data[country] = toks[1:]
  return data

# expected submissions in the format
# Country
# PXapproachN n1 n2 .. n12
# ..
def process_submissions(filepath, official_series):
  submissions = {}
  f = open(filepath, 'r')
  # skip header line
  lines = f.readlines()[1:]
  f.close()
  incountry = ""
  for l in lines:
    l = l.strip()
    if len(l) < 1:
      continue
    toks = re.split("\s+", l)
    if len(toks) == 1 and not re.match(r'P\d',toks[0]):
      incountry = toks[0]
      if incountry not in submissions:
        submissions[incountry] = []
      continue

    if len(toks) == 1 and re.match(r'P\d',toks[0]):
      continue
    
    if not re.match(r'P\dApproach.+',toks[0]):
      raise Exception("Unexpected line " + l)
    toks = re.split("\s+", l)
    appr = toks[0]
    series = toks[1:]
    submissions[incountry] += [{appr:series}]
  return submissions

def score(official, candidate):
  official = [int(x) + 0.0 for x in official]
  candidate = [int(x) + 0.0 for x in candidate]
  if len(official) > len(candidate):
    raise Exception("Series mismatch ", official, candidate)
  candidate = candidate[0:len(official)]
  diff = [official[i] - candidate[i] for i in range(0, len(official))]
  diffoverofficial = [diff[i]/official[i] for i in range(0, len(diff))]
  #official - candidate
  diffoverofficialsq = [x**2 for x in diffoverofficial]
  # adding 0.0 indicates that we don't want integer division
  score = sum(diffoverofficialsq)/len(diffoverofficialsq)
  print official
  print candidate
  print score
  sys.exit(2)
  return score

def rank(official_series, submissions):
  countries = official_series.keys()
  for c in countries:
    bmark = official_series[c]
    candidates = submissions[c]
    for c in candidates:
      appr = c.keys()[0]
      mark = score(bmark, c[appr])
      print mark

if __name__ == "__main__":
  official_series = process_benchmark(benchmark)
  submissions = process_submissions(submissions, official_series)
  #print official_series
  #print submissions['ES']
  rank(official_series, submissions)
