FROM nginx:stable-alpine

COPY dist /usr/share/nginx/html
COPY ./src/http-common.js /usr/share/nginx/html/src/

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]