#!/usr/bin/env bash

{ # this ensures the entire script is downloaded #

# OS specific support (must be 'true' or 'false').
cygwin=false;
darwin=false;
solaris=false;
freebsd=false;
case "$(uname)" in
    CYGWIN*)
        cygwin=true
        ;;
    Darwin*)
        darwin=true
        ;;
    SunOS*)
        solaris=true
        ;;
    FreeBSD*)
        freebsd=true
esac

# Global variables
SUR_VERSION="0.1.0"
SUR_SERVICE="http://get.daplay.cl/"
SUR_PLATFORM=$(uname)

# Set $SUR_HOME
if [ -z "$SUR_HOME" ]; then
  SUR_HOME="$HOME/.sur"

  if [[ "$cygwin" == 'true' ]]; then
  	echo "Cygwin detected - normalizing paths for unzip..."
  	SUR_HOME=$(cygpath -w "$SUR_HOME")
  fi
fi

# Local variables
sur_bin_folder="${SUR_HOME}/bin"
sur_tmp_folder="${SUR_HOME}/tmp"
sur_zip_file="${sur_tmp_folder}/sur-${SUR_VERSION}.zip"
sur_archives_folder="${SUR_HOME}/archives"

# Profile envs
sur_bash_profile="${HOME}/.bash_profile"
sur_profile="${HOME}/.profile"
sur_bashrc="${HOME}/.bashrc"
sur_zshrc="${HOME}/.zshrc"

sur_init_snippet=$( cat << EOF
#THIS MUST BE AT THE END OF THE FILE FOR Sur TO WORK!!!
export SUR_HOME="$SUR_HOME"
export PATH=\$PATH:$SUR_HOME/bin
EOF
)



# Sanity checks

echo "Looking for a previous installation of Sur..."
if [ -d "$SUR_HOME" ]; then
	echo "Sur found."
	echo ""
	echo "======================================================================================================"
	echo " You already have Sur installed."
	echo " Sur was found at:"
	echo ""
	echo "    ${SUR_HOME}"
	echo ""
	echo " Please remove installation to continue."
	# echo " Please consider running the following if you need to upgrade."
	# echo ""
	# echo "    $ sur selfupdate force"
	# echo ""
	# echo "======================================================================================================"
	# echo ""
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


echo "Installing Sur scripts..."


# Create directory structure

echo "Create distribution directories..."
mkdir -p "$sur_bin_folder"
mkdir -p "$sur_tmp_folder"
mkdir -p "$sur_archives_folder"

# 1) descarga el ultimo zip a $SUR_HOME/tmp/sur-0.1.0.zip
echo "Download script archive..."
curl --location --progress-bar "${SUR_SERVICE}/sur-${SUR_VERSION}.zip" > "$sur_zip_file"

# 2) descomprime el zip a $SUR_HOME/tmp/stage
echo "Extract script archive..."

if [[ "$cygwin" == 'true' ]]; then
	echo "Cygwin detected - normalizing paths for unzip..."
	sur_zip_file=$(cygpath -w "$sur_zip_file")
fi

# UNZIP
unzip -qo "$sur_zip_file" -d "$SUR_HOME"
rm -rf $SUR_HOME/bin $SUR_HOME/lib
mv $SUR_HOME/sur-$SUR_VERSION/* $SUR_HOME
rm -rf $SUR_HOME/sur-$SUR_VERSION/

echo "Attempt update of interactive bash profile..."
touch "${sur_bashrc}"
if [[ -z $(grep SUR_HOME "$sur_bashrc") ]]; then
    echo -e "\n$sur_init_snippet" >> "$sur_bashrc"
    echo "Added sur init snippet to $sur_bashrc"
fi

echo "Attempt update of zsh profile..."
touch "$sur_zshrc"
if [[ -z $(grep SUR_HOME "$sur_zshrc") ]]; then
    echo -e "\n$sur_init_snippet" >> "$sur_zshrc"
    echo "Updated existing ${sur_zshrc}"
fi

echo -e "\n\n\nAll done!\n\n"

echo "Please open a new terminal:"
echo ""
echo "    source \"$$HOME}/\""
echo ""
echo "Then issue the following command:"
echo ""
echo "    sur help"
echo ""
echo "Enjoy!!!"

} # this ensures the entire script is downloaded #
