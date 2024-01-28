# Stage 1: Build the application
FROM node:14.17.0-alpine3.13 AS build
WORKDIR /usr/src/app
COPY package.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Serve the React Application from NodeJS server
FROM node:14.17.0-alpine3.13
WORKDIR /usr/src/app
COPY --from=build /usr/src/app/build ./build
COPY package.json ./
RUN npm install -g serve
EXPOSE 3000
CMD ["serve", "-s", "build"]

# The first stage of the Dockerfile uses the official Node image to build the React application.
# The second stage uses the official Node image again to serve the React application from NodeJS server.