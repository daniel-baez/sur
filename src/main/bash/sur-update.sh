#!/usr/bin/env sh

echo "Looking for $SUR_HOME..."
if [ -z "$SUR_HOME" ]; then
	echo "Environment variable SUR_HOME, Not found."
	echo "======================================================================================================"
	echo " Please check your Sur installation"
	echo "======================================================================================================"
	echo ""
	exit 0
fi

echo "Looking for unzip..."
if [ -z $(which unzip) ]; then
	echo "Not found."
	echo "======================================================================================================"
	echo " Please install unzip on your system using your favourite package manager."
	echo ""
	echo " Restart after installing unzip."
	echo "======================================================================================================"
	echo ""
	exit 0
fi

echo "Looking for zip..."
if [ -z $(which zip) ]; then
	echo "Not found."
	echo "======================================================================================================"
	echo " Please install zip on your system using your favourite package manager."
	echo ""
	echo " Restart after installing zip."
	echo "======================================================================================================"
	echo ""
	exit 0
fi

echo "Looking for curl..."
if [ -z $(which curl) ]; then
	echo "Not found."
	echo ""
	echo "======================================================================================================"
	echo " Please install curl on your system using your favourite package manager."
	echo ""
	echo " Restart after installing curl."
	echo "======================================================================================================"
	echo ""
	exit 0
fi

if [[ "$solaris" == true ]]; then
	echo "Looking for gsed..."
	if [ -z $(which gsed) ]; then
		echo "Not found."
		echo ""
		echo "======================================================================================================"
		echo " Please install gsed on your solaris system."
		echo ""
		echo " Sur uses gsed extensively."
		echo ""
		echo " Restart after installing gsed."
		echo "======================================================================================================"
		echo ""
		exit 0
	fi
else
	echo "Looking for sed..."
	if [ -z $(which sed) ]; then
		echo "Not found."
		echo ""
		echo "======================================================================================================"
		echo " Please install sed on your system using your favourite package manager."
		echo ""
		echo " Restart after installing sed."
		echo "======================================================================================================"
		echo ""
		exit 0
	fi
fi

SUR_VERSION=@SUR_VERSION@

last_version=$(curl -o- http://get.daplay.cl/sur.all 2>/dev/null | tail -n 1)

if [ "x$last_version" = "x$SUR_VERSION" ]; then
    echo "Sur is up to date."
    exit 0
fi

curl http://get.daplay.cl/sur-$last_version.zip --output "sur-$last_version.zip" --silent
unzip -qo "sur-$last_version.zip" -d "$SUR_HOME"
rm -rf $SUR_HOME/bin $SUR_HOME/lib
mv "$SUR_HOME/sur-$last_version"/* "$SUR_HOME"/
rm -rf $SUR_HOME/sur-$last_version/
rm -rf "sur-$last_version.zip"

echo "Sur updated."
