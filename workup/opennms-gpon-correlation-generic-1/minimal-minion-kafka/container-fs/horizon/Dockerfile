# installs minimal perl to allow send-event.pl to work

FROM opennms/horizon:33.0.5

USER root

RUN microdnf -y install perl-libwww-perl perl-Sys-Hostname && \
    rm -rf /var/cache/yum
    
USER 10001