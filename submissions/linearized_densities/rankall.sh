#!/bin/bash
../../scripts/python/rank_density_scores.py -i unempl_scored_raw -f no > unempl_density_scored
../../scripts/python/rank_density_scores.py -i hicp_scored_raw -f yes > hicp_density_scored
../../scripts/python/rank_density_scores.py -i hicpxnrg_scored_raw -f yes > hicpxnrg_density_scored
../../scripts/python/rank_density_scores.py -i tourall_scored_raw -f no > tourall_density_scored
../../scripts/python/rank_density_scores.py -i tourhot_scored_raw -f no > tourhot_density_scored
../../scripts/python/rank_density_scores.py -i trade_scored_raw -f yes > trade_density_scored
../../scripts/python/rank_density_scores.py -i tradexfuel_scored_raw -f yes > tradexfuel_density_scored
