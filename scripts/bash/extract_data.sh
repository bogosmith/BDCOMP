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

if [ -d "${outfull}" ]; then
  datefull
fi

mkdir ${outfull}
echo $outfull
tm="&time=2015M11&time=2015M12&time=2016M01&time=2016M02&time=2016M03&time=2016M04&time=2016M05&time=2016M06&time=2016M07&time=2016M08&time=2016M09&time=2016M10&time=2016M11&time=2016M12"

# Track 1
wget -O ${outfull}/unem_nsa http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/une_nb_m?age=TOTAL\&sex=T\&s_adj=NSA"$tm"
wget -O ${outfull}/unem_sa http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/une_nb_m?age=TOTAL\&sex=T\&s_adj=SA\&"$tm"

# Track 2
wget -O ${outfull}/hicp http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/prc_hicp_midx?age=TOTAL\&coicop=CP00\&unit=I05"$tm"

# Track 3
wget -O ${outfull}/hicp_x_nrg http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/prc_hicp_midx?age=TOTAL\&coicop=TOT_X_NRG\&unit=I05"$tm"

# Track 4
wget -O ${outfull}/tour_accom http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/tour_occ_nim?unit=NR\&nace_r2=I551\-I553\&indic_to=B006"$tm"

# Track 5
wget -O ${outfull}/tour_hotel http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/tour_occ_nim?unit=NR\&nace_r2=I551\&indic_to=B006"$tm"

# Track 6
wget -O ${outfull}/retail_gross http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/sts_trtu_m?indic_bt=TOVT\&s_adj=GROSS\&nace_r2=G47"$tm"

#wget -O ${outfull}/retail_adj http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/sts_trtu_m?indic_bt=TOVV\&s_adj=SWDA\&nace_r2=G47"$tm"

wget -O ${outfull}/retail_adj http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/sts_trtu_m?nace_r2=G47\&indic_bt=TOVV\&s_adj=SWDA"$tm"

# Track 7

wget -O ${outfull}/retail_nofuel_gross http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/sts_trtu_m?indic_bt=TOVT\&s_adj=GROSS\&nace_r2=G47_X_G473"$tm"

wget -O ${outfull}/retail_nofuel_adj http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/sts_trtu_m?nace_r2=G47_X_G473\&indic_bt=TOVV\&s_adj=SWDA"$tm"

#echo $str
#$str

#wget -O ${outfull}/unem_nsa http://ec.europa.eu/eurostat/wdds/rest/data/v2.1/unicode/en/une_nb_m?age=TOTAL\&sex=T\&s_adj=NSA$tm
