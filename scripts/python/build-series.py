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
for i in range(1,12):
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
    appr = l[:l.index(":")]
    gr = re.search("raw:[\d\.]+",l).group(0)
    mean = gr[4:]
    return appr, mean 

def process_file(filepath, track, month, series):
  f = open(filepath, 'r')
  lines = f.readlines()
  f.close()
  intrack = False
  incountry = ""
  for l in lines:
    if l.startswith("Track" + track):
      curr_track = l[:6]
      if track == curr_track:
          intarck = True
          continue
      else:
          intrack = False
          continue
    if intrack:
       if l[:2] in geo:
           incountry = l[:2]
           continue
           
    if intrack and incountry:
        if len(l) > 0:
            appr, mean = process_line(l)
            if month == 1:
                series[incountry] = {}
                series[incountry][approach] = {}
            series[incountry][approach][month] = mean
    

def process_dir(participant_directory, track):
  res = {}
  rounds = os.listdir(participant_directory)
  # sort by filename to that round2.txt comes before round10.txt
  rounds.sort(key = lambda x:int(x[5:x.index(".")]))
  for r in rounds:
      file = participant_directory + "\\" + r
      process_file(file, track, months[r], res)
  return res

parts = os.listdir(directory)
for p in parts:
  print process_dir(directory + "\\" + p,track)


