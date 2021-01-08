FROM nginx:1.19.0-alpine

RUN rm /etc/nginx/conf.d/default.conf
RUN apk add nano
COPY nginx.conf /etc/nginx/conf.d
