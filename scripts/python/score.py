#!/usr/bin/python

import getopt
import sys
import os
import re
import operator
import math

usage = """
  python <script-name> -b benchmark -s submissions -f <yes|no> "floating point or not" -r<yes|no> "whether to round official data or not" <-d<yes|no>> "directional accuracy or not"
"""
directional = "no"
opts,args = getopt.getopt(sys.argv[1:], "b:s:f:r:d:")
for o,a in opts:
    if o == "-b":
      benchmark = a
    elif o == "-s":
      submissions = a
    elif o == "-d":
      directional = a
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

if directional == "yes":
  directional = True
elif directional == "no":
  directional = False
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
      incountry = substitute(incountry)
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

def convert_to_growth_ind(x):
    #print x
    y = [x[i] - x[i-1] for i in range(1,len(x))]
    # this gives a list of 0s, 1s and -1s depending on whether the value stays, raises or falls
    y = [t and (1,-1)[t < 0] for t in y]
    #print y
    return y

def score(official, candidate, directional):
  if not floating:
    official = [int(x) + 0.0 for x in official]
    candidate = [int(x) + 0.0 for x in candidate]
  else:
    official = [float(x) + 0.0 for x in official]
    candidate = [float(x) + 0.0 for x in candidate]
  if rounding and floating:
    official = [round(x,1) for x in official]
  if len(official) > len(candidate):
    raise Exception("Series mismatch ", official, candidate)
  candidate = candidate[0:len(official)]
  if (directional):
    x = convert_to_growth_ind(official)
    y = convert_to_growth_ind(candidate)
    # return the percentage of correctly guessed directions
    #return sum([x[i] == y[i] for i in range(0,len(x))])*100.0/(len(x) - 1)
    score = sum([x[i] == y[i] for i in range(0,len(x))])
    #return score*100.0/len(x)
    return score*1.0/len(x)

  else:
    diff = [official[i] - candidate[i] for i in range(0, len(official))]
    diffoverofficial = [diff[i]/official[i] for i in range(0, len(diff))]
    #official - candidate
    diffoverofficialsq = [x**2 for x in diffoverofficial]
    # adding 0.0 indicates that we don't want integer division
    score = sum(diffoverofficialsq)/len(diffoverofficialsq)
    return score

def disp_val_array(x, flt, rnd, directional):
  # First to compare directions always round because of issues around 0
  if directional and flt:
    rnd = True
  if flt and not rnd:
    x = [round(float(t),8) for t in x]
    #x = [float(t) for t in x]
  elif flt and rnd:
    x = [round(float(t),1) for t in x]
  else:
    x = [int(t) for t in x]
  if directional:
    x = convert_to_growth_ind(x)
    # no growth indicator for the first month
    x = ["*"] + x
  x = str(x)
  return x[1:-1].replace(",", " ").replace("'","")

def rank(official_series, submissions, directional):
  countries = sorted(official_series.keys())
  for ctry in countries:
    bmark = official_series[ctry]
    if ctry not in submissions:
      continue
    candidates = submissions[ctry]
    marks = {}
    for c in candidates:
      approach_name = c.keys()[0]
      sc = score(bmark, c[approach_name], directional)
      marks[approach_name] = sc
      #for a in c.keys():
      #  sc = score(bmark,c[a])
      #  marks[a] = sc
    scored = sorted(marks.items(), key = operator.itemgetter(1), reverse = directional)
    print ctry, ("RRMSE","ratio_correct")[directional] ,disp_val_array(bmark, floating, rounding, directional)
    for a in scored:
      series = []
      for c in candidates:
        if c.keys()[0] == a[0]:
          series = c[a[0]]
      #filter(lambda c: c.keys()[0] == a[0], candidates)
      #print a[0], [float(x) for x in series], round(float(a[1]), 6)
      #print a[0], disp_val_array(series, floating), round(float(a[1]), 8)
      # print the score in a format depending on whether a directional or a normal evaluation is being done
      print a[0], (math.sqrt(round(float(a[1]), 8)),a[1])[directional], disp_val_array(series, floating, False, directional)
    #print scored

if __name__ == "__main__":
  official_series = process_benchmark(benchmark)
  submissions = process_submissions(submissions, official_series)
  #print official_series
  #print submissions['ES']
  rank(official_series, submissions, directional)
