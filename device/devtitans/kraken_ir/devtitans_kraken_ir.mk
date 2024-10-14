# Herda as configurações do emulador (produto sdk_phone_x86_64)
$(call inherit-product, $(SRC_TARGET_DIR)/product/sdk_phone_x86_64.mk)

# Sobrescreve algumas variáveis com os dados do novo produto
PRODUCT_NAME := devtitans_kraken_ir
PRODUCT_DEVICE := kraken_ir
PRODUCT_BRAND := KrakenIrBrand
PRODUCT_MODEL := KrakenIrModel

PRODUCT_PACKAGES += \
	logging_test \
	smart_ir_lib \
	smart_ir_client

# App Privilegiado de Teste do Serviço Smartlamp
# PRODUCT_PACKAGES += SmartlampTestApp

# App Privilegiado de Teste do Serviço SmartIR
PRODUCT_PACKAGES += IRApp

# App Privilegiado de Teste do Serviço SmartIR
PRODUCT_PACKAGES += KrakenIRapp 

BOARD_SEPOLICY_DIRS += device/devtitans/kraken_ir/sepolicy

# Smartir AIDL Interface
PRODUCT_PACKAGES += devtitans.smartir

# Smartir Binder Service
PRODUCT_PACKAGES += devtitans.smartir-service

# Device Framework Matrix (Declara que o nosso produto Kraken precisa do serviço smartir)
DEVICE_FRAMEWORK_COMPATIBILITY_MATRIX_FILE := device/devtitans/kraken_ir/device_framework_matrix.xml

# Manager
PRODUCT_PACKAGES += devtitans.smartirmanager
