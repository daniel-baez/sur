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
SUR_VERSION="0.1.5"
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


sur_init_snippet=$( cat << EOF
#THIS MUST BE AT THE END OF THE FILE FOR Sur TO WORK!!!
export SUR_HOME="$SUR_HOME"
export PATH=\$PATH:\$SUR_HOME/bin
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

nvm_try_profile() {
  if [ -z "${1-}" ] || [ ! -f "${1}" ]; then
    return 1
  fi
  echo "${1}"
}

#
# Detect profile file if not specified as environment variable
# (eg: PROFILE=~/.myprofile)
# The echo'ed path is guaranteed to be an existing file
# Otherwise, an empty string is returned
#
nvm_detect_profile() {
  if [ -n "${PROFILE}" ] && [ -f "${PROFILE}" ]; then
    echo "${PROFILE}"
    return
  fi

  local DETECTED_PROFILE
  DETECTED_PROFILE=''
  local SHELLTYPE
  SHELLTYPE="$(basename "/$SHELL")"

  if [ "$SHELLTYPE" = "bash" ]; then
    if [ -f "$HOME/.bashrc" ]; then
      DETECTED_PROFILE="$HOME/.bashrc"
    elif [ -f "$HOME/.bash_profile" ]; then
      DETECTED_PROFILE="$HOME/.bash_profile"
    fi
  elif [ "$SHELLTYPE" = "zsh" ]; then
    DETECTED_PROFILE="$HOME/.zshrc"
  fi

  if [ -z "$DETECTED_PROFILE" ]; then
    for EACH_PROFILE in ".profile" ".bashrc" ".bash_profile" ".zshrc"
    do
      if DETECTED_PROFILE="$(nvm_try_profile "${HOME}/${EACH_PROFILE}")"; then
        break
      fi
    done
  fi

  if [ ! -z "$DETECTED_PROFILE" ]; then
    echo "$DETECTED_PROFILE"
  fi
}


# Create directory structure

echo "Create distribution directories..."
mkdir -p "$sur_bin_folder"
mkdir -p "$sur_tmp_folder"
mkdir -p "$sur_archives_folder"

# 1) descarga el ultimo zip a $SUR_HOME/tmp/sur-0.1.5.zip
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

echo "Attempt update of interactive shell profile..."
# Profile envs
sur_bashrc=$(nvm_detect_profile)
if [[ -z $(grep SUR_HOME "$sur_bashrc") ]]; then
    echo -e "\n$sur_init_snippet" >> "$sur_bashrc"
    echo "Added sur init snippet to $sur_bashrc"
    \. "$sur_bashrc"
fi


echo -e "\n\n\nAll done!\n\n"

echo "Please open a new terminal, or run the following in the existing one:"
echo ""
echo "    source \"${sur_bashrc}\""
echo ""
echo "Then issue the following command:"
echo ""
echo "    sur help"
echo ""
echo "Enjoy!!!"

} # this ensures the entire script is downloaded #
