#!/usr/bin/python

import getopt
import sys
import os
import re

usage = """
  python <script-name> -d directory -t track
"""

geo = ["EU", "EU28" "EA19", "EA-18", "AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "GR", "HU", "IE", "IT", "LV", "LT", "NL", "PL", "PT", "RO", "SK", "SI", "ES", "SE", "UK"]
months = {}
for i in range(1,13):
    months["round"+str(i)+".txt"]=i

opts,args = getopt.getopt(sys.argv[1:], "d:t:")
for o,a in opts:
    if o == "-d":
      directory = a
    elif o == "-t":
      track = a
    else:
      print usage
      sys.exit()
try:
  directory;track
except NameError:
  print usage
  sys.exit()

def process_line(l):
    #print l
    appr = l[:l.index(":")]
    #print "DDD"
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
      print r
      file = participant_directory + "/" + r
      process_file(file, track, months[r], res)
  return res

def pretty_print(processed):
  countries = processed.keys()
  for c in countries:
    print c
    for approach in processed[c].keys():
      print approach
      values = ""
      for val in processed[c][approach].keys():
        values += str(processed[c][approach][val]) + " "
      print values

parts = os.listdir(directory)
for p in parts:
  processed = process_dir(directory + "/" + p,track)
  #print processed['AT']
  print p
  pretty_print(processed)

