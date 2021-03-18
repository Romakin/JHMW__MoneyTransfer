#!/bin/sh

rm -rf ./../release
rm -rf ./release

cp -r ./../MoneyTransfer ./../release
mv -v ./../release ./../MoneyTransfer
cd release
echo $PWD
rm ./scripts/BuildTestContainersMacOS.sh


echo "<Building container..>"

./mvnw clean package
echo "prepare Dockerfile"
cp ./scripts/Dockerfile ./scripts/Dockerfile_BACKUP
sed -i '' -e '2s/.*/EXPOSE 5500/1' \
    -e '3s/\*jarName\*/demo-1\.0-SNAPSHOT/1' ./scripts/Dockerfile

mv ./target/demo-1.0-SNAPSHOT.jar ./scripts
cd scripts
if [[ "$(docker images -q MTSapp:latest 2> /dev/null)" != "" ]]; then
  docker rmi MTSapp:latest
fi
docker build -t MTSapp .
cd ..
rm ./scripts/demo-1.0-SNAPSHOT.jar

echo "</Container with name 'MTSapp' ready!>"