install:
	install -m 755 -o root -g root -d $(RPM_INSTALL_ROOT)/var/run/iotsysd/
	install -m 755 -o root -g root -d $(RPM_INSTALL_ROOT)/etc/iotsysd/
	install -m 755 -o root -g root -d $(RPM_INSTALL_ROOT)/etc/init.d/
	install -m 755 -o root -g root -d $(RPM_INSTALL_ROOT)/etc/iotsysd/device/
	install -m 755 -o root -g root -d $(RPM_INSTALL_ROOT)/etc/iotsysd/log/
	install -m 755 -o root -g root -d $(RPM_INSTALL_ROOT)/etc/iotsysd/bin/
	install -m 755 -o root -g root -d $(RPM_INSTALL_ROOT)/var/log/iotsysd/
	install -m 755 bin/iotsysd $(RPM_INSTALL_ROOT)/usr/local/bin/iotsysd
	install -m 755 iotsysd.jar $(RPM_INSTALL_ROOT)/etc/iotsysd/bin/iotsysd.jar
	install -m 755 init/iotsysd $(RPM_INSTALL_ROOT)/etc/init.d/iotsysd
	install -m 644 etc/*.properties $(RPM_INSTALL_ROOT)/etc/iotsysd/
	install -m 644 etc/device/*.properties $(RPM_INSTALL_ROOT)/etc/iotsysd/device/
	install -m 644 etc/log/*.properties $(RPM_INSTALL_ROOT)/etc/iotsysd/log/
