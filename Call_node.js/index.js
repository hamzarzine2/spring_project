
const eureka = require('eureka-js-client');
const axios = require('axios');

const eurekaClient = new eureka.Eureka({
    instance: {
        app: 'node-client',
        hostName: 'localhost:10000',
        ipAddr: '127.0.0.1',
        statusPageUrl: 'http://localhost:10000/info',
        port: {
            '$': 10000,
            '@enabled': true,
        },
        vipAddress: 'node-client',
        dataCenterInfo: {
            '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
            name: 'MyOwn',
        }
    },
    eureka: {
        host: 'localhost',
        port: 9000,
        servicePath: '/eureka/apps/',
    }
});



// Initialiser le client Eureka
eurekaClient.start(() => {
    performApiRequest();
});

// Nom du service à découvrir dans Eureka

// Fonction pour découvrir une instance du service dans Eureka
function getInvestorService(serviceName) {
    return new Promise((resolve, reject) => {
        const instances = eurekaClient.getInstancesByAppId(serviceName);
        //console.log("instancesss ", instances);
        if (instances.length > 0) {
            // Récupération de l'adresse et du port de la première instance
            const instance = instances[0];
            const serviceUrl = `http://${instance.ipAddr}:${instance.port.$}`;
            resolve(serviceUrl);
        } else {
            reject(new Error('Aucune instance du service trouvée dans Eureka'));
        }
    });
}

function getWalletService(serviceName) {
    return new Promise((resolve, reject) => {
        const instances = eurekaClient.getInstancesByAppId(serviceName);
        //console.log("instancesss ", instances);
        if (instances.length > 0) {
            // Récupération de l'adresse et du port de la première instance
            const instance = instances[0];
            const serviceUrl = `http://${instance.ipAddr}:${instance.port.$}`;
            resolve(serviceUrl);
        } else {
            reject(new Error('Aucune instance du service trouvée dans Eureka'));
        }
    });
}

// Fonction pour effectuer une requête HTTP vers l'API
async function performApiRequest() {
    try {
        // Découverte de l'instance du service
        const serviceUrlInv = await getInvestorService('investor');
        const serviceUrlWallet = await getWalletService('wallet');

        // Effectuer la requête HTTP
        //const urlWallet = await axios.get(serviceUrlWallet);

        console.log(`${serviceUrlInv}/investor/All`);
        console.log(`${serviceUrlWallet}/wallet/Name`);

        const investorsResponse = await axios.get(`${serviceUrlInv}/investor/all`);
        const investors = await investorsResponse.data;

        let totalInterests = 0
        for (const investor of investors) {

            const walletResponse = await axios.get(`${serviceUrlWallet}/wallet/${investor.username}`);
            const walletsInvestor = await walletResponse.data;
            for (const position of walletsInvestor) {
                if(position.quantity < 0){
                    totalInterests = totalInterests+ (position.quantity * position.unitValue * 1/100)
                }
            }
            if(totalInterests != 0 ){

                const response = await axios.post(`${serviceUrlWallet}/wallet/${investor.username}`, 
                    [
                        {
                         ticker: 'CASH',
                         quantity: totalInterests,
                         unitValue :1.0

                        }
                    ]
                ,{
                headers: {
                    'Content-Type': 'application/json',
                },
                }
                );
            const positionsEdited = await response.data
             }
        }
        

        eurekaClient.stop()
    } catch (error) {
        eurekaClient.stop()
        console.error('Erreur lors de la découverte du service ou de la requête API:',error);
    }
}

// Appel de la fonction principale
