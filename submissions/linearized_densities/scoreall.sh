#!/bin/bash
../../scripts/r/density_score_modified.r unempl_linearized unempl_scored_raw
../../scripts/r/density_score_simple.r hicp_linearized hicp_scored_raw
../../scripts/r/density_score_simple.r hicpxnrg_linearized hicpxnrg_scored_raw
../../scripts/r/density_score_modified.r tourall_linearized tourall_scored_raw
../../scripts/r/density_score_modified.r tourhot_linearized tourhot_scored_raw
../../scripts/r/density_score_simple.r trade_linearized trade_scored_raw 
../../scripts/r/density_score_simple.r tradexfuel_linearized tradexfuel_scored_raw
