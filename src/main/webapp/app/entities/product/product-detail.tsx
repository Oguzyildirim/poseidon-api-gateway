import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './product.reducer';
import { IProduct } from 'app/shared/model/product.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProductDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ProductDetail extends React.Component<IProductDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { productEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="poseidonApiGatewayApp.product.detail.title">Product</Translate> [<b>{productEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="levelOne">
                <Translate contentKey="poseidonApiGatewayApp.product.levelOne">Level One</Translate>
              </span>
            </dt>
            <dd>{productEntity.levelOne}</dd>
            <dt>
              <span id="levelTwo">
                <Translate contentKey="poseidonApiGatewayApp.product.levelTwo">Level Two</Translate>
              </span>
            </dt>
            <dd>{productEntity.levelTwo}</dd>
            <dt>
              <span id="levelThree">
                <Translate contentKey="poseidonApiGatewayApp.product.levelThree">Level Three</Translate>
              </span>
            </dt>
            <dd>{productEntity.levelThree}</dd>
            <dt>
              <span id="partNo">
                <Translate contentKey="poseidonApiGatewayApp.product.partNo">Part No</Translate>
              </span>
            </dt>
            <dd>{productEntity.partNo}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="poseidonApiGatewayApp.product.description">Description</Translate>
              </span>
            </dt>
            <dd>{productEntity.description}</dd>
            <dt>
              <span id="uom">
                <Translate contentKey="poseidonApiGatewayApp.product.uom">Uom</Translate>
              </span>
            </dt>
            <dd>{productEntity.uom}</dd>
            <dt>
              <span id="mtmlUom">
                <Translate contentKey="poseidonApiGatewayApp.product.mtmlUom">Mtml Uom</Translate>
              </span>
            </dt>
            <dd>{productEntity.mtmlUom}</dd>
            <dt>
              <span id="explanation">
                <Translate contentKey="poseidonApiGatewayApp.product.explanation">Explanation</Translate>
              </span>
            </dt>
            <dd>{productEntity.explanation}</dd>
            <dt>
              <span id="picture">
                <Translate contentKey="poseidonApiGatewayApp.product.picture">Picture</Translate>
              </span>
            </dt>
            <dd>{productEntity.picture}</dd>
            <dt>
              <span id="information">
                <Translate contentKey="poseidonApiGatewayApp.product.information">Information</Translate>
              </span>
            </dt>
            <dd>{productEntity.information}</dd>
            <dt>
              <Translate contentKey="poseidonApiGatewayApp.product.productCategory">Product Category</Translate>
            </dt>
            <dd>{productEntity.productCategoryLevelOne ? productEntity.productCategoryLevelOne : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/product" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/product/${productEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ product }: IRootState) => ({
  productEntity: product.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProductDetail);
