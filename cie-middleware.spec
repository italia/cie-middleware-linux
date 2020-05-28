Name: cie-middleware
Version: 1.0
Release: 1
Summary: cie-middleware
License: bsd

%description
CIE-middleware

%install
rm -rf %{buildroot}
mkdir -p %{buildroot}/usr/local/lib
mkdir -p %{buildroot}/usr/share
mkdir -p %{buildroot}/usr/share/applications

cp -pr %{_sourcedir}/CIEID %{buildroot}/usr/share
cp -p %{_sourcedir}/cieid.desktop %{buildroot}/usr/share/applications
cp -p %{_sourcedir}/libcie-pkcs11.so %{buildroot}/usr/local/lib


%files
/usr/share/CIEID
/usr/local/lib/libcie-pkcs11.so
/usr/share/applications/cieid.desktop


%changelog
	

