#!/usr/bin/python

import getopt
import sys
import os

usage = """
  python <script-name> -d directory -t track
"""

geo = ["EU", "EU28" "EA19", "EA-18", "AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "GR", "HU", "IE", "IT", "LV", "LT", "NL", "PL", "PT", "RO", "SK", "SI", "ES", "SE", "UK"]
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

def process_file(filepath, track):
  f = open(filepath, 'r')
  res = {}
  lines = f.readlines()
  intrack = False
  for l in lines:
    if l.startswith("Track" + track):
      intrack = True
    if 
     


def process_dir(participant_directory, track):
  
  

parts = os.listdir(directory)
for p in parts:
  process_dir(p,track)


