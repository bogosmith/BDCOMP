#!/usr/bin/python

import getopt
import sys
import os
import re

usage = """
  python <script-name> -d directory -t track -s <sa|nsa>
"""

geo = ["EU", "EU28", "EA19", "EA-18", "AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "EL", "FI", "FR", "DE", "GR", "HU", "IE", "IT", "LV", "LT", "LU", "MT", "NL", "PL", "PT", "RO", "SK", "SI", "ES", "SE", "UK"]
participants = {"DJOLOV" : "P1", "ETLA" : "P2", "JRC" : "P3", "WEIGAND" : "P4", "WBS" : "P5"}
months = {}
for i in range(1,13):
    months["round"+str(i)+".txt"]=i

opts,args = getopt.getopt(sys.argv[1:], "d:t:s:")
for o,a in opts:
    if o == "-d":
      directory = a
    elif o == "-t":
      track = a
    elif o == "-s":
      sa = a
    else:
      print usage
      sys.exit()
try:
  directory;track;sa;
  if sa == "sa":
    sa = True
  elif sa == "nsa":
    sa = False
  else:
    raise NameError("")
except NameError:
  print usage
  sys.exit()

def process_line(l):
    #print l
    appr = l[:l.index(":")]
    appr = "".join([t for t in appr if t!=" "])
    #print "DDD"
    if sa:
      srch = re.search("sa:\s*[\d\.]+",l)
      if not srch:
        return None, None 
      gr = re.search("sa:\s*[\d\.]+",l).group(0)
      mean = gr[3:]
    else:  
      gr = re.search("raw:\s*[\d\.]+",l).group(0)
      mean = gr[4:]
    return appr, mean 

def process_file(filepath, track, month, series):
  f = open(filepath, 'r')
  lines = f.readlines()
  f.close()
  intrack = False
  incountry = ""
  for l in lines:
    if l.startswith("Track"):
      curr_track = l[5]
      if track == curr_track:
          intrack = True
          continue
      else:
          intrack = False
          continue
    if incountry:
      if len(l.strip()) == 0:
        incountry = False
        continue
    if intrack:
       if l.strip() in geo:
           incountry = l.strip()
           continue
           
    if intrack and incountry:
        #print filepath
        if len(l.strip()) > 0:
            approach, mean = process_line(l)
            if month == 1:
                if not series:
                    series[incountry] = {}
                if not incountry in series:
                    series[incountry] = {}
                if not approach in series[incountry]:
                    series[incountry][approach] = {}
            series[incountry][approach][month] = mean
            #print series

def process_dir(participant_directory, track):
  res = {}
  rounds = os.listdir(participant_directory)
  rounds = [x for x in rounds if x[:5] == "round"]
  # sort by filename to that round2.txt comes before round10.txt
  rounds.sort(key = lambda x:int(x[5:x.index(".")]))
  for r in rounds:
      #print r
      file = participant_directory + "/" + r
      process_file(file, track, months[r], res)
  return res

def pretty_print(processed,participant):
  countries = processed.keys()
  for c in countries:
    print c
    #print processed[c].keys()
    #for approach in sorted(processed[c].keys()):
    # ensure numeric sorting by removing letters
    # the 0 at the end ensures that None is transferred to 0 so the code doesn't break
    for approach in sorted(processed[c].keys(), key = lambda x: int(re.sub(r'[a-zA-Z]',"",str(x) + "0"))):
      #print approach
      values = participants[participant]
      if not approach:
        continue
      # remove blanks to make it easy to insert in Excel
      values += "".join([t for t in approach if t!=" "]) + " "
      for val in processed[c][approach].keys():
        values += str(processed[c][approach][val]) + " "
      print values

parts = os.listdir(directory)
for p in parts:
  # p is participant in this function call
  processed = process_dir(directory + "/" + p,track)
  #print processed['AT']
  print participants[p]
  pretty_print(processed,p)

