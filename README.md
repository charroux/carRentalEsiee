# carRentalEsiee

## PUT Request

curl --header "Content-Type: application/json" --request PUT --data '{"debut":"4/11/2024","fin":"20/11/2024"}' 'http://localhost:8080/cars/11AA22?rent=true'

### Launch a workflow when the code is updated

The script is there: https://github.com/charroux/carRentalEsiee/blob/main/.github/workflows/action.yml

Create a new branch on your machine:
```
git branch newcarservice
```
Move to the new branch:
```
git checkout newcarservice
```
Update the code and commit changes:
```
git commit -a -m "newcarservice"
```
Push the changes to GitHub:
```
git push -u origin newcarservice
```
Create a Pull request on GitHub and follow the workflow.

Merge the branch on Github is everything is OK.

Then pull the new main version:

```
git checkout main
```
```
git pull origin main
```

If necessary delete the branch:

```
git branch -D newcarservice
```
```
git push origin --delete newcarservice
```

### Docker

Create a Dockerfile in the code folder: https://github.com/charroux/carRentalEsiee/blob/main/rent/Dockerfile
Build a Docker image:
```
docker build -t rentalservice .      
```
Run the container:
```
docker run -p 4000:8080 rentalservice    
```
Then check in your browser:
```
http://localhost:4000/cars
```

### Publish the Docker image to the Docker Hub

Tager l'image :
```
docker tag 4da2332370c7 votreIdDocherHub/rental:1
```
où le numéro est l'identifiant de l'image donné par docker images, et 1 est un numéro de version

Se connecter au Docker Hub :
```
docker login
```

Publier l'image :
```
docker push votreIdDocherHub/rental:1      
```

### Installer Minikube

https://minikube.sigs.k8s.io/docs/start/?arch=%2Fmacos%2Fx86-64%2Fstable%2Fbinary+download

### Démarrer Minikube
```
minikube start --driver=docker      
```

Combien de noeuds dans le cluster?
```
kubectl get nodes      
```
Désployer votre image Docker :
```
kubectl create deployment rentalservice --image=charroux/rentalservice:1      
```
Attention d'utiliser votre image.

Vérifier que le procesus fonctionne bien :
```
kubectl get pods      
```
Scale :
```
kubectl scale --replicas=2 deployment/rentalservice          
```     

Tuer un pod pour constater son redémarrage:
```
kubectl delete pod rentalservice-5b746d6f65-t5m8v               
```    
Ajouter un load balancer :
```
kubectl expose deployment rentalservice --type=LoadBalancer              
```    
Récupérer l'adresse du service :
```
minikube service rentalservice --url                      
```    

### Deployer via un fichier yaml

```
kubectl apply -f deployment.yml                           
```    

Voir le fichier : https://github.com/charroux/carRentalEsiee/blob/main/deployment.yml

### Inpecter le cluster via le dashboard

```
minikube dashboard
```

### Install Istio
https://istio.io/latest/docs/setup/getting-started/

Download Istio (take care at the version 1.17 here).

```
cd istio-1.17.0    
export PATH=$PWD/bin:$PATH    
istioctl install --set profile=demo -y
cd ..   
```
Enable auto-injection of the Istio side-cars when the pods are started:
```
kubectl label namespace default istio-injection=enabled
```
Install the Istio addons (Kiali, Prometheus, Jaeger, Grafana):
```
kubectl apply -f samples/addons
```
## 
Enable auto-injection of the Istio side-cars when the pods are started:
```
kubectl label namespace default istio-injection=enabled
```

Configure Docker so that it uses the Kubernetes cluster:
```
minikube docker-env
eval $(minikube -p minikube docker-env)
eval $(minikube docker-env)  
```

### Kubernetes Gateway

Check the configuration at 53: https://github.com/charroux/carRentalEsiee/blob/main/deployment.yml

Check also where the Kubernetes service is registered in the gataway at line 72.

Apply again the config:
```
kubectl apply -f deployment.yml      
```

Then get the address of the gateway:
```
kubectl -n istio-system port-forward deployment/istio-ingressgateway 31380:8080  
```

and finally test in your browser:
http://localhost:31380/rentalservice/cars

### Service mesh

Already included into Istio

#### Display the Kiali dashboard
Kiali is a console for Istio service mesh.
```
kubectl -n istio-system port-forward deployment/kiali 20001:20001
```
Launch the console: http://localhost:20001/

Active again carservice:

http://localhost:31380/carservice/cars

Then inspect the cluster in Kiali.

#### Monitoring with Graphana
```
kubectl -n istio-system port-forward deployment/grafana 3000:3000
```
http://localhost:3000/


#### Monitoring with Jaeger
```
kubectl port-forward -n istio-system $(kubectl get pod -n istio-system -l app=jaeger -o jsonpath='{.items[0].metadata.name}') 16686:16686
```
Launch the console: http://localhost:16686/

