FROM node:16.16.0-alpine

WORKDIR /home/app/client

COPY package.json ./
RUN npm install
COPY . .
RUN npm run build
ADD ./.env /home/app/client/.env
CMD [ "npm", "start" ]
