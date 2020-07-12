/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import ClusterDetailComponent from '@/entities/cluster/cluster-details.vue';
import ClusterClass from '@/entities/cluster/cluster-details.component';
import ClusterService from '@/entities/cluster/cluster.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Cluster Management Detail Component', () => {
    let wrapper: Wrapper<ClusterClass>;
    let comp: ClusterClass;
    let clusterServiceStub: SinonStubbedInstance<ClusterService>;

    beforeEach(() => {
      clusterServiceStub = sinon.createStubInstance<ClusterService>(ClusterService);

      wrapper = shallowMount<ClusterClass>(ClusterDetailComponent, {
        store,
        localVue,
        provide: { clusterService: () => clusterServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCluster = { id: 123 };
        clusterServiceStub.find.resolves(foundCluster);

        // WHEN
        comp.retrieveCluster(123);
        await comp.$nextTick();

        // THEN
        expect(comp.cluster).toBe(foundCluster);
      });
    });
  });
});
