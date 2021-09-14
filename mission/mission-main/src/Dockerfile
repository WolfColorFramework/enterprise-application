FROM openjdk:8-jre
WORKDIR /home/app
ENV base_dir /home/app
ADD pis-base.jar $base-dir
RUN ["mv","pis-base.jar","pis.jar"]
ADD custom.yml $base-dir
ADD logback.xml $base-dir
ADD application.properties $base-dir
VOLUME ../app
EXPOSE 9800
ENTRYPOINT ["java","-jar"]
CMD ["pis.jar"]