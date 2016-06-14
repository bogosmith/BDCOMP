#!/bin/bash

usage() { echo "Usage: $0 -o <output_dir>" 1>&2; exit 1; }
nodir() { echo "Output directory has to exist." 1>&2; exit 1; }
datefull() { echo "Today's directory exists. Quitting.." 1>&2; exit 1; }


while getopts ":o:" o; do
    case "${o}" in
       o) 
          outdir=${OPTARG}
          ;;
       *)
          usage
          ;;
    esac
done	
shift $((OPTIND-1))

if [ -z "${outdir}" ]; then
  usage
fi

if [ ! -d "${outdir}" ]; then
  nodir
fi

today=$(date +%Y-%m-%d)
outfull=$outdir/$today
outjson=$outfull/json
outuni=$outfull/unicode
outsdmx=$outfull/sdmx

if [ -d "${outfull}" ]; then
  datefull
fi

mkdir ${outfull}
mkdir ${outjson}
mkdir ${outuni}
mkdir ${outsdmx}

echo $outfull
tm="&time=2015M11&time=2015M12&time=2016M01&time=2016M02&time=2016M03&time=2016M04&time=2016M05&time=2016M06&time=2016M07&time=2016M08&time=2016M09&time=2016M10&time=2016M11&time=2016M12"

# Track 1
wget -O ${outuni}/unem_nsa http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/une_rt_m?unit=THS_PER\&age=TOTAL\&sex=T\&s_adj=NSA"$tm"
wget -O ${outjson}/unem_nsa http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/json/en/une_rt_m?unit=THS_PER\&age=TOTAL\&sex=T\&s_adj=NSA"$tm"
wget -O ${outsdmx}/unem_nsa_compressed http://ec.europa.eu/eurostat/SDMX/diss-web/rest/data/une_rt_m/M.NSA.TOTAL.THS_PER.T.?startPeriod=2015-12-01\&endPeriod=2016-12-31
xmllint --format ${outsdmx}/unem_nsa_compressed --output ${outsdmx}/unem_nsa

wget -O ${outuni}/unem_sa http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/une_rt_m?unit=THS_PER\&age=TOTAL\&sex=T\&s_adj=SA\&"$tm"
wget -O ${outjson}/unem_sa http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/json/en/une_rt_m?unit=THS_PER\&age=TOTAL\&sex=T\&s_adj=SA\&"$tm"
wget -O ${outsdmx}/unem_sa_compressed http://ec.europa.eu/eurostat/SDMX/diss-web/rest/data/une_rt_m/M.SA.TOTAL.THS_PER.T.?startPeriod=2015-12-01\&endPeriod=2016-12-31
xmllint --format ${outsdmx}/unem_sa_compressed --output ${outsdmx}/unem_sa

exit 2
# Track 2
wget -O ${outuni}/hicp http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/prc_hicp_midx?coicop=CP00\&unit=I05"$tm"
wget -O ${outjson}/hicp http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/json/en/prc_hicp_midx?coicop=CP00\&unit=I05"$tm"
wget -O ${outsdmx}/hicp_compressed http://ec.europa.eu/eurostat/SDMX/diss-web/rest/data/prc_hicp_midx/M.I05.CP00..?startPeriod=2015-12-01\&endPeriod=2016-12-31
xmllint --format ${outsdmx}/hicp_compressed --output ${outsdmx}/hicp


# Track 3
wget -O ${outuni}/hicp_x_nrg http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/prc_hicp_midx?age=TOTAL\&coicop=TOT_X_NRG\&unit=I15"$tm"
wget -O ${outjson}/hicp_x_nrg http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/json/en/prc_hicp_midx?age=TOTAL\&coicop=TOT_X_NRG\&unit=I15"$tm"
wget -O ${outsdmx}/hicp_x_nrg_compressed http://ec.europa.eu/eurostat/SDMX/diss-web/rest/data/prc_hicp_midx/M.I15.TOT_X_NRG..?startPeriod=2015-12-01\&endPeriod=2016-12-31
xmllint --format ${outsdmx}/hicp_x_nrg_compressed --output ${outsdmx}/hicp_x_nrg


# Track 4
wget -O ${outuni}/tour_accom http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/tour_occ_nim?unit=NR\&nace_r2=I551\-I553\&indic_to=B006"$tm"
wget -O ${outjson}/tour_accom http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/json/en/tour_occ_nim?unit=NR\&nace_r2=I551\-I553\&indic_to=B006"$tm"
wget -O ${outsdmx}/tour_accom_compressed http://ec.europa.eu/eurostat/SDMX/diss-web/rest/data/tour_occ_nim/M.B006.NR.I551-I553.?startPeriod=2015-12-01\&endPeriod=2016-12-31
xmllint --format ${outsdmx}/tour_accom_compressed --output ${outsdmx}/tour_accom


# Track 5
wget -O ${outuni}/tour_hotel http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/tour_occ_nim?unit=NR\&nace_r2=I551\&indic_to=B006"$tm"
wget -O ${outjson}/tour_hotel http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/json/en/tour_occ_nim?unit=NR\&nace_r2=I551\&indic_to=B006"$tm"
wget -O ${outsdmx}/tour_hotel_compressed http://ec.europa.eu/eurostat/SDMX/diss-web/rest/data/tour_occ_nim/M.B006.NR.I551.?startPeriod=2015-12-01\&endPeriod=2016-12-31
xmllint --format ${outsdmx}/tour_hotel_compressed --output ${outsdmx}/tour_hotel

# Track 6
#wget -O ${outuni}/retail_gross http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/sts_trtu_m?indic_bt=TOVT\&s_adj=GROSS\&nace_r2=G47"$tm"
#wget -O ${outjson}/retail_gross http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/json/en/sts_trtu_m?indic_bt=TOVT\&s_adj=GROSS\&nace_r2=G47"$tm"
#wget -O ${outsdmx}/retail_gross_compressed http://ec.europa.eu/eurostat/SDMX/diss-web/rest/data/sts_trtu_m/M.TOVT.G47.GROSS.?startPeriod=2015-12-01\&endPeriod=2016-12-31
wget -O ${outuni}/retail_gross http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/sts_trtu_m?indic_bt=TOVT\&s_adj=NSA\&nace_r2=G47"$tm"
wget -O ${outjson}/retail_gross http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/json/en/sts_trtu_m?indic_bt=TOVT\&s_adj=NSA\&nace_r2=G47"$tm"
wget -O ${outsdmx}/retail_gross_compressed http://ec.europa.eu/eurostat/SDMX/diss-web/rest/data/sts_trtu_m/M.TOVT.G47.NSA.I10.?startPeriod=2015-12-01\&endPeriod=2016-12-31
xmllint --format ${outsdmx}/retail_gross_compressed --output ${outsdmx}/retail_gross


#wget -O ${outuni}/retail_adj http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/sts_trtu_m?nace_r2=G47\&indic_bt=TOVV\&s_adj=SWDA"$tm"
#wget -O ${outjson}/retail_adj http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/json/en/sts_trtu_m?nace_r2=G47\&indic_bt=TOVV\&s_adj=SWDA"$tm"
#wget -O ${outsdmx}/retail_adj_compressed http://ec.europa.eu/eurostat/SDMX/diss-web/rest/data/sts_trtu_m/M.TOVV.G47.SWDA.?startPeriod=2015-12-01\&endPeriod=2016-12-31
wget -O ${outuni}/retail_adj http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/sts_trtu_m?nace_r2=G47\&indic_bt=TOVV\&s_adj=SCA"$tm"
wget -O ${outjson}/retail_adj http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/json/en/sts_trtu_m?nace_r2=G47\&indic_bt=TOVV\&s_adj=SCA"$tm"
wget -O ${outsdmx}/retail_adj_compressed http://ec.europa.eu/eurostat/SDMX/diss-web/rest/data/sts_trtu_m/M.TOVV.G47.SCA.I10.?startPeriod=2015-12-01\&endPeriod=2016-12-31
xmllint --format ${outsdmx}/retail_adj_compressed --output ${outsdmx}/retail_adj

# Track 7

#wget -O ${outuni}/retail_nofuel_gross http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/sts_trtu_m?indic_bt=TOVT\&s_adj=GROSS\&nace_r2=G47_X_G473"$tm"
#wget -O ${outjson}/retail_nofuel_gross http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/json/en/sts_trtu_m?indic_bt=TOVT\&s_adj=GROSS\&nace_r2=G47_X_G473"$tm"
#wget -O ${outsdmx}/retail_nofuel_gross_compressed http://ec.europa.eu/eurostat/SDMX/diss-web/rest/data/sts_trtu_m/M.TOVT.G47_X_G473.GROSS.?startPeriod=2015-12-01\&endPeriod=2016-12-31
wget -O ${outuni}/retail_nofuel_gross http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/sts_trtu_m?indic_bt=TOVT\&s_adj=NSA\&nace_r2=G47_X_G473"$tm"
wget -O ${outjson}/retail_nofuel_gross http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/json/en/sts_trtu_m?indic_bt=TOVT\&s_adj=NSA\&nace_r2=G47_X_G473"$tm"
wget -O ${outsdmx}/retail_nofuel_gross_compressed http://ec.europa.eu/eurostat/SDMX/diss-web/rest/data/sts_trtu_m/M.TOVT.G47_X_G473.NSA.I10.?startPeriod=2015-12-01\&endPeriod=2016-12-31
xmllint --format ${outsdmx}/retail_nofuel_gross_compressed --output ${outsdmx}/retail_nofuel_gross

#wget -O ${outuni}/retail_nofuel_adj http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/sts_trtu_m?nace_r2=G47_X_G473\&indic_bt=TOVV\&s_adj=SWDA"$tm"
#wget -O ${outjson}/retail_nofuel_adj http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/json/en/sts_trtu_m?nace_r2=G47_X_G473\&indic_bt=TOVV\&s_adj=SWDA"$tm"
#wget -O ${outsdmx}/retail_nofuel_adj_compressed http://ec.europa.eu/eurostat/SDMX/diss-web/rest/data/sts_trtu_m/M.TOVV.G47_X_G473.SWDA.?startPeriod=2015-12-01\&endPeriod=2016-12-31
wget -O ${outuni}/retail_nofuel_adj http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/sts_trtu_m?nace_r2=G47_X_G473\&indic_bt=TOVV\&s_adj=SCA"$tm"
wget -O ${outjson}/retail_nofuel_adj http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/json/en/sts_trtu_m?nace_r2=G47_X_G473\&indic_bt=TOVV\&s_adj=SCA"$tm"
wget -O ${outsdmx}/retail_nofuel_adj_compressed http://ec.europa.eu/eurostat/SDMX/diss-web/rest/data/sts_trtu_m/M.TOVV.G47_X_G473.SCA.I10.?startPeriod=2015-12-01\&endPeriod=2016-12-31
xmllint --format ${outsdmx}/retail_nofuel_adj_compressed --output ${outsdmx}/retail_nofuel_adj

#echo $str
#$str

#wget -O ${outfull}/unem_nsa http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/une_nb_m?age=TOTAL\&sex=T\&s_adj=NSA$tm
