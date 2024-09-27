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
