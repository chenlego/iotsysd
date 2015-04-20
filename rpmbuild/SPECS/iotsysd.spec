%define iotsysd_log_dir /var/log/iotsysd
%define iotsysd_conf_dir /etc/iotsysd
%define iotsysd_bin_dir /etc/iotsysd/bin

Summary: The "IoT System" program from spinhat
Name: iotsysd
Version: 1.0
Release: 1
Source: %{name}-%{version}.tar.gz
License: GPLv3+
Group: System Environment/Daemons
Url: http://www.spinhat.org

# dependancy
BuildRoot: %{_tmppath}/%{name}-%{version}-%{release}-root

Prereq: /sbin/chkconfig
Prereq: /sbin/service
Prereq: fileutils

Requires: java-1.8.0-openjdk >= 1:1.8.0.45

Requires(post): info
Requires(preun): info

%description 
iotsysd is developed by spinhat, it is a IoT Server that can communicated with devices of AndrewTech.

%prep
%setup -q

%build
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
#make

%install
rm -rf %{buildroot}
mkdir -p %{buildroot}/etc/iotsysd
mkdir -p %{buildroot}/etc/iotsysd/log
mkdir -p %{buildroot}/etc/iotsysd/device
mkdir -p %{buildroot}/usr/local/bin
make install RPM_INSTALL_ROOT=%{buildroot}

%post
/sbin/chkconfig --add %{name}
/sbin/chkconfig --level 2345 %{name} on

%files
%dir %attr(0755, nobody, nobody) %{iotsysd_log_dir}
%dir %attr(0755, root, root) %{iotsysd_conf_dir}
%dir %attr(0755, root, root) %{iotsysd_conf_dir}/device
%dir %attr(0755, root, root) %{iotsysd_conf_dir}/log
%dir %attr(0755, nobody, nobody) /var/run/iotsysd
%attr(0755, root, root) /etc/init.d/%{name}
%attr(0755, nobody, nobody) %{iotsysd_bin_dir}/%{name}.jar
%attr(0755, nobody, nobody) /usr/local/bin/%{name}
%attr(0644, root, root) %{iotsysd_conf_dir}/*.properties
%attr(0644, root, root) %{iotsysd_conf_dir}/device/*.properties
%attr(0644, root, root) %{iotsysd_conf_dir}/log/*.properties

%changelog
* Sun Apr 06 2015 Lego Chen <legochen@spinhat.org> 1.0
- Initial version of the package
