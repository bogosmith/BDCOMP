#!/bin/bash
../../scripts/python/linearize_density_submissions.py -b ../../benchmarks/unempl/nsa/benchmark -s ../unempl_density -f yes -r no > unempl_linearized
../../scripts/python/linearize_density_submissions.py -b ../../benchmarks/hicp/benchmark -s ../hicp_density -f yes -r yes > hicp_linearized
../../scripts/python/linearize_density_submissions.py -b ../../benchmarks/hicpxnrj/benchmark -s ../hicpxnrg_density -f yes -r yes > hicpxnrg_linearized
../../scripts/python/linearize_density_submissions.py -b ../../benchmarks/tourhotel/benchmark -s ../tourhot_density -f yes -r no > tourhot_linearized
../../scripts/python/linearize_density_submissions.py -b ../../benchmarks/tourall/benchmark -s ../touraccom_density -f yes -r no > tourall_linearized
../../scripts/python/linearize_density_submissions.py -b ../../benchmarks/trade/benchmark -s ../retail_density -f yes -r yes > trade_linearized
../../scripts/python/linearize_density_submissions.py -b ../../benchmarks/tradexfuel/benchmark -s ../retailnoauto_density -f yes -r yes > tradexfuel_linearized

