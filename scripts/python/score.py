#!/usr/bin/python

import getopt
import sys
import os
import re
import operator

usage = """
  python <script-name> -b benchmark -s submissions -f <yes|no> "floating point or not"
"""
opts,args = getopt.getopt(sys.argv[1:], "b:s:f:")
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
    else:
      print usage
      sys.exit()
try:
  benchmark;submissions;floating 
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
  if not floating:
    official = [int(x) + 0.0 for x in official]
    candidate = [int(x) + 0.0 for x in candidate]
  else:
    official = [float(x) + 0.0 for x in official]
    candidate = [float(x) + 0.0 for x in candidate]
  if len(official) > len(candidate):
    raise Exception("Series mismatch ", official, candidate)
  candidate = candidate[0:len(official)]
  diff = [official[i] - candidate[i] for i in range(0, len(official))]
  diffoverofficial = [diff[i]/official[i] for i in range(0, len(diff))]
  #official - candidate
  diffoverofficialsq = [x**2 for x in diffoverofficial]
  # adding 0.0 indicates that we don't want integer division
  score = sum(diffoverofficialsq)/len(diffoverofficialsq)
  #print official
  #print candidate
  #print score
  #sys.exit(2)
  return score

def disp_val_array(x, flt):
  if flt:
    x = [round(float(t),8) for t in x]
    #x = [float(t) for t in x]
  else:
    x = [int(t) for t in x]
  x = str(x)
  return x[1:-1].replace(",", " "
)
def rank(official_series, submissions):
  countries = official_series.keys()
  for ctry in countries:
    bmark = official_series[ctry]
    if ctry not in submissions:
      continue
    candidates = submissions[ctry]
    marks = {}
    for c in candidates:
      approach_name = c.keys()[0]
      sc = score(bmark, c[approach_name])
      marks[approach_name] = sc
      #for a in c.keys():
      #  sc = score(bmark,c[a])
      #  marks[a] = sc
    scored = sorted(marks.items(), key = operator.itemgetter(1))
    print ctry, disp_val_array(bmark, floating)
    for a in scored:
      series = []
      for c in candidates:
        if c.keys()[0] == a[0]:
          series = c[a[0]]
      #filter(lambda c: c.keys()[0] == a[0], candidates)
      #print a[0], [float(x) for x in series], round(float(a[1]), 6)
      print a[0], disp_val_array(series, floating), round(float(a[1]), 8)
    #print scored

if __name__ == "__main__":
  official_series = process_benchmark(benchmark)
  submissions = process_submissions(submissions, official_series)
  #print official_series
  #print submissions['ES']
  rank(official_series, submissions)
