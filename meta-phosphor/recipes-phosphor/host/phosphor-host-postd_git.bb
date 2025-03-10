SUMMARY = "Phosphor OpenBMC Post Code Daemon"
DESCRIPTION = "Phosphor OpenBMC Post Code Daemon"
HOMEPAGE = "http://github.com/openbmc/phosphor-host-postd"
PR = "r1"
PV = "0.1+git${SRCPV}"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

inherit meson
inherit pkgconfig
inherit systemd

PACKAGECONFIG ?= ""
PACKAGECONFIG[7seg] = "-D7seg=enabled,-D7seg=disabled,,udev"

DEPENDS += "sdbusplus"
DEPENDS += "sdeventplus"
DEPENDS += "phosphor-dbus-interfaces"
DEPENDS += "systemd"
DEPENDS += "libgpiod"

S = "${WORKDIR}/git"
SRC_URI = "git://github.com/openbmc/phosphor-host-postd;branch=master;protocol=https"
SRCREV = "4f26b3ee7de4e9a38acae3d7487ce746c3f656f7"

SNOOP_DEVICE ?= "aspeed-lpc-snoop0"
POST_CODE_BYTES ?= "1"
7SEG_GPIO ?= "0"

SERVICE_FILE = "lpcsnoop.service"
SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE:${PN} += "${SERVICE_FILE}"

EXTRA_OEMESON:append = " \
    -Dsnoop-device=${SNOOP_DEVICE} \
    -Dpost-code-bytes=${POST_CODE_BYTES} \
    -Dsystemd-target=multi-user.target \
    -Dtests=disabled \
"

POSTCODE_SEVENSEG_DEVICE ?= "seven_seg_disp_val"
SERVICE_FILE_7SEG = " \
  postcode-7seg@.service \
  postcode-7seg@${POSTCODE_SEVENSEG_DEVICE}.service \
"
SYSTEMD_SERVICE:${PN} += "${@bb.utils.contains('PACKAGECONFIG', '7seg', '${SERVICE_FILE_7SEG}', '', d)}"
